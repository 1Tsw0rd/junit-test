package site.metacoding.junittest.util;

import org.springframework.stereotype.Component;

//가짜
@Component  //IoC컨테이너 등록
public class MailSenderStub implements MailSender { //stub는 가정이라는 뜻
    @Override
    public boolean send() {
        return true;
    }
}
