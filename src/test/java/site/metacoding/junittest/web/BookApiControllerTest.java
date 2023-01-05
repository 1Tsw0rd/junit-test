package site.metacoding.junittest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import site.metacoding.junittest.service.BookService;
import site.metacoding.junittest.web.dto.request.BookSaveReqDto;

import static org.assertj.core.api.Assertions.*;

//통합테스트(C, S, R)
//컨트롤러만 테스트하는 것 아님
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
