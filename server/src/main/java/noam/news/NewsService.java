package noam.news;

import noam.news.dto.Article;
import noam.news.dto.NewsApiResponse;
import noam.news.dto.SearchDetails;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {

//    private final static String NEWS_API_KEY = "9539d471a87f4ba88b30afb07f4fbaf0";
    private final static String NEWS_API_KEY = "4b3f0432ba9b4434b139958c4bac84eb";
    private final String API_BASE_URL_EVERYTHING = "https://newsapi.org/v2/everything?apiKey=" + NEWS_API_KEY;
    private final String API_BASE_URL_TOP = "https://newsapi.org/v2/top-headlines?&q=apple&apiKey=" + NEWS_API_KEY;
    private final String LATEST_POSSIBLE_DATE_STRING = "2022-10-28";
    private final RestTemplate restTemplate;

    private SearchDetails lastRequestDetails = new SearchDetails();


    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Article> getAllArticlesByDetails(String searchText, Integer startIndex, Integer endIndex, String fromDate, String toDate) {
        List<Article> articles;
        validateInputs(startIndex, endIndex, fromDate,toDate);
        if (needToSentHttpRequest(searchText, fromDate, toDate)) {
            String url = getUpdatedUrl(searchText, fromDate, toDate);
            articles = getArticlesFromNewsApi(url);
            updatelastRequestDetails(articles, searchText, fromDate, toDate);
        }
        articles = getRelevantArticleList(startIndex, endIndex);
        return articles;


    }
    public List<Article> getTopHeadlines(){
        return getArticlesFromNewsApi(API_BASE_URL_TOP);
    }

    private void validateInputs(Integer startIndex, Integer endIndex, String fromDateString, String toDateString){
        if((startIndex != null && endIndex != null && startIndex > endIndex) || (startIndex != null && startIndex < 0) || (endIndex != null && endIndex < 1))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Illegal indexes");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date latestDatePossible = dateFormat.parse(LATEST_POSSIBLE_DATE_STRING);
            if(fromDateString != null){
                Date fromDate = dateFormat.parse(fromDateString);
                if(toDateString != null){
                    Date toDate = dateFormat.parse(toDateString);
                    if(toDate.before(fromDate))
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Illegal Dates");
                }
                if(fromDate.before(latestDatePossible))
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Illegal Dates");
            }
            if(toDateString != null){
                Date toDate = dateFormat.parse(toDateString);
                if(toDate.before(latestDatePossible))
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Illegal Dates");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updatelastRequestDetails(List<Article> articles, String searchText, String fromDate, String toDate) {
        if (articles != null) {
            lastRequestDetails.setArticles(articles);
            lastRequestDetails.setSearchText(searchText);
            lastRequestDetails.setFromDate(fromDate);
            lastRequestDetails.setToDate(toDate);
        }
    }

    private List<Article> getRelevantArticleList(Integer startIndex, Integer endIndex) {
        int articlesListSize = lastRequestDetails.getArticles().size();
        if (startIndex == null )
            startIndex = 0;

        if (endIndex == null)
            endIndex = articlesListSize;

        if (endIndex > articlesListSize)
            endIndex = articlesListSize;

        if(startIndex > articlesListSize)
            return List.of();


        return lastRequestDetails.getArticles().subList(startIndex, endIndex);
    }

    private Boolean needToSentHttpRequest(String searchText, String fromDate, String toDate) {
        return !this.lastRequestDetails.equals(new SearchDetails(searchText,fromDate,toDate));
    }

    private String getUpdatedUrl(String searchText, String fromDate, String toDate) {
        String url = API_BASE_URL_EVERYTHING;
        if (searchText != null && searchText.length() > 0) {
            url = String.format("%s%s%s%s", url, "&", "q=", searchText);
        }
        if (fromDate != null) {
            url = String.format("%s%s%s%s", url, "&", "from=", fromDate);
        }
        if (toDate != null) {
            url = String.format("%s%s%s%s", url, "&", "to=", toDate);
        }
        return url;
    }

    private List<Article> getArticlesFromNewsApi(String url) {
        ResponseEntity<NewsApiResponse> responseEntity = restTemplate.getForEntity(url, NewsApiResponse.class);
        NewsApiResponse newsApiResponse = responseEntity.getBody();
        return newsApiResponse == null ? Collections.emptyList() : newsApiResponse.getArticles();
    }

}
