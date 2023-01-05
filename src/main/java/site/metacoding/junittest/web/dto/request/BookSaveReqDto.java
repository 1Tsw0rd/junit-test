package site.metacoding.junittest.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.junittest.domain.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter //Controller에서 Setter가 호출되면서 Dto에 값 채워짐
public class BookSaveReqDto {
    @Size(min = 1, max = 50) //Book엔티티 참고
    @NotBlank //널과 공백 검사
    private String title;
    @Size(min = 2, max = 20)
    @NotBlank
    private String author;

    public Book toEntity(){
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
