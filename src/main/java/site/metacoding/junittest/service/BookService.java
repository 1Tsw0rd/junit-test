package site.metacoding.junittest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.metacoding.junittest.domain.Book;
import site.metacoding.junittest.domain.BookRepository;
import site.metacoding.junittest.web.dto.BookRespDto;
import site.metacoding.junittest.web.dto.BookSaveReqDto;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    //1. 책등록
    @Transactional(rollbackOn = RuntimeException.class) //런타입예외 발생 시 롤백
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPS = bookRepository.save(dto.toEntity());
        return new BookRespDto().toDto(bookPS);
    }

    //2. 책목록보기

    //3. 책 한건 보기

    //4. 책 삭제

    //5. 책수정
}
