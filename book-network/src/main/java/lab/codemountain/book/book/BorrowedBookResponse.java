package lab.codemountain.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private double rating;
    private boolean returned;
    private boolean returnApproved;
}
