package noam.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDetails {

    private List<Article> articles;
    private String searchText;
    private String fromDate;
    private String toDate;

    public SearchDetails(String searchText, String fromDate, String toDate) {
        this.searchText = searchText;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public boolean equals(SearchDetails other){
        boolean isToDate = (this.toDate == null && other.toDate == null) ||
                (this.toDate != null && other.toDate != null && this.toDate.equals(other.toDate));
        boolean isFromDate = (this.fromDate == null && other.fromDate == null) ||
                (this.fromDate != null && other.fromDate != null && this.fromDate.equals(other.fromDate));
        boolean isSearchText = (this.searchText == null && other.searchText == null) ||
                (this.searchText != null && other.searchText != null && this.searchText.equals(other.searchText));
        return isToDate && isFromDate && isSearchText;
    }
}
