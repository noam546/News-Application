package noam.news;

import noam.news.dto.Article;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/news")
public class NewsController {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping(path="/everything")
    public ResponseEntity<?> getAllArticlesByDetails(
            @RequestParam String searchText,
            @RequestParam(required = false) Integer startIndex,
            @RequestParam(required = false) Integer endIndex,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = YYYY_MM_DD) String fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = YYYY_MM_DD) String toDate
            ) {
        try {
            List<Article> response = newsService.getAllArticlesByDetails(searchText, startIndex, endIndex, fromDate, toDate);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException ex) {
            return generateErrorResponse(ex);
        }
    }

        @GetMapping(path = "/top-headlines")
    public ResponseEntity<?> getTopHeadlines(){
        try{
            List<Article> response = newsService.getTopHeadlines();
            return ResponseEntity.ok(response);
        }catch (HttpClientErrorException ex){
            return generateErrorResponse(ex);
        }
    }


    public ResponseEntity<?> generateErrorResponse(HttpClientErrorException ex){
        switch (ex.getStatusCode().value()) {
            case 400 -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The request was unacceptable, often due to a missing or misconfigured parameter");
            }
            case 401 -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized for this action");
            }
            case 429 -> {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("You made too many requests within a window of time and have been rate limited. Back off for a while");
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            }
        }
    }

}
