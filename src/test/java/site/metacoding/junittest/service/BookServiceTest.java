package site.metacoding.junittest.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import site.metacoding.junittest.domain.Book;
import site.metacoding.junittest.domain.BookRepository;
import site.metacoding.junittest.util.MailSender;

import site.metacoding.junittest.web.dto.BookRespDto;
import site.metacoding.junittest.web.dto.BookSaveReqDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //가짜 메모리 환경 만들어짐
public class BookServiceTest {
    @InjectMocks //아래 Mock 객체들 갖다 쓰게 됨(의존성 생김).. 주입하라 mock들을..
    private BookService bookService;

    @Mock //가짜 객체가 뜸
    private BookRepository bookRepository; //인터페이스

    @Mock
    private MailSender mailSender;  //인터페이스

    @Test
    public void 책등록하기_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit강의");
        dto.setAuthor("메타코딩");

        //stub 가짜행동정의 - 서비스에서는 가짜가 필요
        //when : 이게 실행되면, thenReturn : 실제로 동작했을 때 예상되는 반환 값
        //any()는 테스트이기에 일단 아무거나 집어넣어도 thenReturn 값으로 반환된다고 가정할거니까
        when(bookRepository.save(any())).thenReturn(dto.toEntity());//data가 잘 저장되면
        when(mailSender.send()).thenReturn(true); //메일 전송 잘 될거라고
        //가짜행동정의

        //when 테스트 ㄱㄱ
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        //then - dto 우리가 예상하는 값, bookRespDto 실제 DB에 있는 값
        //assertEquals(dto.getTitle(), bookRespDto.getTitle());
        //assertEquals(dto.getAuthor(), bookRespDto.getAuthor());

        //위 방법 헷갈린다고 아래방법 추천함(실제값, 내가 넣은 값)
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트() {
        //given(파라미터로 들어올 데이터)

        //stub(가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "강의11", "메타코딩"));
        books.add(new Book(2L, "강의22", "겟인데어"));
        when(bookRepository.findAll()).thenReturn(books);

        //when(실행)
        List<BookRespDto> bookRespDtoList = bookService.책목록보기();

        //print
        bookRespDtoList.stream().forEach((dto) -> {
            System.out.println("=============테스트");
                    System.out.println(dto.getId());
                    System.out.println(dto.getTitle());
                }
        );

        //then(검증)
        assertThat(bookRespDtoList.get(0).getTitle()).isEqualTo("강의11");
        assertThat(bookRespDtoList.get(0).getAuthor()).isEqualTo("메타코딩");
        assertThat(bookRespDtoList.get(1).getTitle()).isEqualTo("강의22");
        assertThat(bookRespDtoList.get(1).getAuthor()).isEqualTo("겟인데어");
    }

    @Test
    public void 책한건보기_테스트(){
        //given
        Long id = 1L;
        Book book = new Book(1L, "강의11", "메타코딩");
        Optional<Book> bookOP = Optional.of(book); //아래 findById는 Optional 반환

        //stud
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto dto = bookService.책한건보기(id);

        //then
        assertThat(dto.getTitle()).isEqualTo(bookOP.get().getTitle());
    }

    @Test
    public void 책수정하기_테스트(){
        //given
        Long id = 1L;
        BookSaveReqDto dto =  new BookSaveReqDto();
        dto.setTitle("스프링부트");
        dto.setAuthor("가브리엘");

        //stub
        Book book = new Book(1L, "강의11", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.책수정하기(id, dto);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

}
