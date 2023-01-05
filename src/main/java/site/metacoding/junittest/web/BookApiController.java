package site.metacoding.junittest.web;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.metacoding.junittest.domain.Book;
import site.metacoding.junittest.service.BookService;
import site.metacoding.junittest.web.dto.response.BookListRespDto;
import site.metacoding.junittest.web.dto.response.BookRespDto;
import site.metacoding.junittest.web.dto.request.BookSaveReqDto;
import site.metacoding.junittest.web.dto.response.CMRespDto;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;

    //1. 책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        //AOP처리하는게 좋음
        if (bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) { //Valid 에러가 여러개 걸릴 수 잇음
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("============================");
            System.out.println(errorMap.toString()); //true 반환하면 에러 발생된 것
            System.out.println("============================");

            throw new RuntimeException(errorMap.toString()); //바로 예외 터트리면 됨
        }

        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);

         return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(),
                 HttpStatus.CREATED); //201 = insert
    }

    //2. 책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList(){
        BookListRespDto bookList = bookService.책목록보기();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 목록 불러오기 성공").body(bookList).build(),
                HttpStatus.OK); //200 = ok
    }

    //3. 책 한건 보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookone(@PathVariable Long id){
        BookRespDto bookRespDto = bookService.책한건보기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 한건 불러오기 성공").body(bookRespDto).build(),
                HttpStatus.OK); //200 = ok
    }

    //4. 책 삭제
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 삭제 완료").body(null).build(),
                HttpStatus.OK); //200 = ok
    }

    //5. 책수정
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto,
                                        BindingResult bindingResult){
        //AOP처리하는게 좋음
        if (bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) { //Valid 에러가 여러개 걸릴 수 잇음
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString()); //바로 예외 터트리면 됨
        }

        BookRespDto bookRespDto = bookService.책수정하기(id, bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 수정 완료").body(bookRespDto).build(),
                HttpStatus.OK); //200 = ok
    }
}
