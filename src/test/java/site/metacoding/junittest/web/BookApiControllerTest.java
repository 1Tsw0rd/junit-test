package site.metacoding.junittest.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import site.metacoding.junittest.domain.Book;
import site.metacoding.junittest.domain.BookRepository;
import site.metacoding.junittest.service.BookService;
import site.metacoding.junittest.web.dto.request.BookSaveReqDto;

import static org.assertj.core.api.Assertions.*;

//통합테스트(C, S, R)
//컨트롤러만 테스트하는 것 아님
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //통합테스트 설정
public class BookApiControllerTest {
    @Autowired
    private TestRestTemplate rt; //HTTP 요청 후 데이터 응답 받을 수 있는 템플릿 객체

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll //1번만 실행
    public static void init(){  //static 먼저 메모리에 1번만 띄워짐
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //http 헤더 Content-Type에 json 타입 넣어줌
    }

    @Autowired
    private BookRepository bookRepository;

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

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test(){
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.DELETE, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        assertThat(code).isEqualTo(1);
        //System.out.println("deleteBook_test : " + response.getStatusCode());
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateBook_test() throws Exception {
        //given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("메타코딩");

        String body = om.writeValueAsString(bookSaveReqDto); //json으로 변환

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.PUT, request, String.class);

        //System.out.println(response.getStatusCodeValue());
        //System.out.println(response.getBody());

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("spring");
    }


    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test(){  //getBookOne_test 시작 전 BeforeEach 시작함! 이 모든 것 전에 테이블을 초기화 한번 함
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookList_test() throws Exception {
        //given
        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers); //Get 방식은 body가 없기에 null
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Test
    public void saveBook_test() throws Exception { //통합테스트에는 stub이 필요없다, 전체를 메모리에 다 띄우고 시작
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링1강");
        bookSaveReqDto.setAuthor("겟인데어");

        String body = om.writeValueAsString(bookSaveReqDto); //json으로 변환됨

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        //response body 값 json으로 파싱해서 dc라는 메모리에 넣어주는 듯
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("스프링1강");
        assertThat(author).isEqualTo("겟인데어");
    }


}
