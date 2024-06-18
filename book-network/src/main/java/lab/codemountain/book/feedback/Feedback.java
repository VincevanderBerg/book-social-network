package lab.codemountain.book.feedback;

import jakarta.persistence.Entity;
import lab.codemountain.book.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double rating;
    private String comment;
}
