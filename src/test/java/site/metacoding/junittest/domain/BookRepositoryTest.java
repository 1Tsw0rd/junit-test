package site.metacoding.junittest.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest //DB와 관련된 컴포넌트만 메모리에 로딩, 컨트롤러와 서비스는 메모리에 뜨지 않음
public class BookRepositoryTest {


    @Autowired //DI 테스트할 땐 @Autowired 쓰는게 좋음
    private BookRepository bookRepository;

    //1. 책 등록
    @Test
    public void 책등록_test(){
        // given(데이터 준비)
        String title = "혼공자";
        String author = "메타코딩";

        //원래 Dto로 받아야 하나, Test 이기에 바로 Builder 사용
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when(테스트 실행)
        Book bookPS = bookRepository.save(book);
        //PS(Persistence) 영구적으로 저장되었다는 뜻,
        //Book book은 DB에 저장된 값이니까 영속화가 된 데이터
        //save(book)과 다르다.

        // then(검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
        //assertEquals(expected, actual) 기대하는 값(title)과 실제 값(bookPS.getTile()) 비교
    }

    //2. 책 목록보기

    //3. 책 한건 보기

    //4. 책 수정

    //5. 책 삭제
    
}
