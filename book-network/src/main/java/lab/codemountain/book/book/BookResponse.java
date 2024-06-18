package lab.codemountain.book.book;

import lombok.Builder;

@Builder
public record BookResponse(
    Long id,
    String title,
    String authorName,
    String isbn,
    String synopsis,
    String owner,
    byte[] bookCover,
    double rating,
    boolean archived,
    boolean shareable
) {
}
