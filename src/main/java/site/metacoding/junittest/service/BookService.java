package site.metacoding.junittest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.metacoding.junittest.domain.Book;
import site.metacoding.junittest.domain.BookRepository;
import site.metacoding.junittest.util.MailSender;
import site.metacoding.junittest.web.dto.response.BookListRespDto;
import site.metacoding.junittest.web.dto.response.BookRespDto;
import site.metacoding.junittest.web.dto.request.BookSaveReqDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;

    //1. 책등록
    @Transactional(rollbackOn = RuntimeException.class) //런타입예외 발생 시 롤백
    public BookRespDto 책등록하기(BookSaveReqDto dto) {
        Book bookPS = bookRepository.save(dto.toEntity());
        if (bookPS != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return bookPS.toDto();
    }

    //2. 책목록보기
    public BookListRespDto 책목록보기() {
        //본코드에 문제 있나?
        List<BookRespDto> dtos = bookRepository.findAll().stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        //print
        dtos.stream().forEach((dto) -> {
                    System.out.println("============본코드");
                    System.out.println(dto.getId());
                    System.out.println(dto.getTitle());

                }
        );

        BookListRespDto bookListRespDto = BookListRespDto.builder()
                .bookList(dtos)
                .build();

        return bookListRespDto;
    }

    //3. 책 한건 보기
    public BookRespDto 책한건보기(Long id) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) { //찾았다면 실행
            return bookOP.get().toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    //4. 책 삭제
    @Transactional(rollbackOn = RuntimeException.class)
    public void 책삭제하기(Long id) {
        bookRepository.deleteById(id);
        //넘겨준 id가 null 이면 IllegalArgumentException 예외 발생, 근데 이건 컨트롤러에서 검사 한번 진행될 것임
        // id를 없는 값으로 넣어주면 동작되긴 한다.
    }

    //5. 책수정
    @Transactional(rollbackOn = RuntimeException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) { //값이 있다면
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 책을 찾을 수 없습니다.");
        }
    } //메서드 종료 시에 더티체킹(flsuh)으로 update 됨

}
