package site.metacoding.junittest.web.dto;

import lombok.Setter;
import site.metacoding.junittest.domain.Book;

@Setter //Controller에서 Setter가 호출되면서 Dto에 값 채워짐
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity(){
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
