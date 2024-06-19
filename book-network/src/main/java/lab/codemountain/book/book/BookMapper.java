package lab.codemountain.book.book;

import lab.codemountain.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .owner(book.getOwner().getUsername())
                .bookCover(book.getBookCover().getBytes())
                .rating(book.getRating())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
       return BorrowedBookResponse.builder()
               .id(history.getId())
               .title(history.getBook().getTitle())
               .authorName(history.getBook().getAuthorName())
               .isbn(history.getBook().getIsbn())
               .rating(history.getBook().getRating())
               .returned(history.isReturned())
               .returnApproved(history.isReturnApproved())
               .build();
    }
}
