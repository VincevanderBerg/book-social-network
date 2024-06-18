package lab.codemountain.book.book;

import jakarta.persistence.*;
import lab.codemountain.book.common.BaseEntity;
import lab.codemountain.book.feedback.Feedback;
import lab.codemountain.book.history.BookTransactionHistory;
import lab.codemountain.book.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRating() {
        if (this.feedbacks == null || this.feedbacks.isEmpty()) {
            return 0.0;
        }

        double rating = this.feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);

        return Math.round(rating * 10.0) / 10.0;
    }
}
