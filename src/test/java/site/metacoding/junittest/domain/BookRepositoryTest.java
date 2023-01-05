package site.metacoding.junittest.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest //DB와 관련된 컴포넌트만 메모리에 로딩, 컨트롤러와 서비스는 메모리에 뜨지 않음
public class BookRepositoryTest {


    @Autowired //DI 테스트할 땐 @Autowired 쓰는게 좋음
    private BookRepository bookRepository;

    //@BeforeAll //테스트 시작 전에 한번만 실행
    @BeforeEach //각 테스트 시작전에 한번씩 실행
    public void 데이터준비(){
        // 잘 되는지 체크System.out.println("===================");
        String title = "junit";
        String author = "겟인데어";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);

    } //트랜잭션 종료 됐다면 말이 안됨
    //가정 1 : T [데이터준비() + 1 책등록], T [데이터준비() + 2. 책목록보기]  << 이렇게 동작함
    //가정 2 : T [데이터준비() + 1 책등록 + 데이터준비() + 2.책목록보기]


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
    } //트랜잭션 종료(저장된 데이터 초기화)

    //2. 책 목록보기
    @Test
    public void 책목록보기_test(){
        //given
        String title = "junit";
        String author = "겟인데어";

        //when
        List<Book> booksPS= bookRepository.findAll();

        System.out.println("사이즈 ========================= :" + booksPS.size());

        //then
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }//트랜잭션 종료(저장된 데이터 초기화)

    //3. 책 한건 보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test() {
        //given
        String title = "junit";
        String author = "겟인데어";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }//트랜잭션 종료(저장된 데이터 초기화)

    //4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        //given
        Long id = 1L;
        //when
        bookRepository.deleteById(id);

        //then
        //Optional<Book> bookPS1 = bookRepository.findById(id);
        //Optional은 null값도 받음

        assertFalse(bookRepository.findById(id).isPresent());
        //delete는 반환되는 값이 없어야 성공
        //assertFalse()는 false 일 때 true
        //isPresent()는 값이 없으면 true 반환(값 있니?)
    }


    //5. 책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test(){
        //현재 1, junit, 겟인데어
        //given
        Long id = 1L;
        String title = "junit5";
        String author = "메타코딩";
        Book book = new Book(id, title, author);

        //when
        Book bookPS = bookRepository.save(book);

//        bookRepository.findAll().stream()
//                .forEach(b -> {
//                    System.out.println("id : " + b.getId() + "  title : " + b.getTitle() + " author : " + b.getAuthor());
//                    System.out.println("========================");
//                }
//        );
//        System.out.println("id : " + bookPS.getId() + "  title : " + bookPS.getTitle() + " author : " + bookPS.getAuthor());
//        System.out.println("========================");

        //then
        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

}
