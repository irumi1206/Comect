package PoolC.Comect.email;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@AllArgsConstructor
public class RegisterMail {

    private JavaMailSender emailSender;

    public MimeMessage createMessage(String to, String id) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message= emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Linky 회원가입 이메일 인증");

        String msgg="";
        msgg+="<a href=\""+"http://linky.linky-authentication.site:8081/authenticationCheck?id="+id+"\">여기를 클릭해주세요!</a>";
        message.setText(msgg,"utf-8","html");
        message.setFrom(new InternetAddress("linky_mail@naver.com","linky_authentication"));

        return message;
    }

    public MimeMessage passwordChangeMessage(String to, int randomNumber) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message= emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Linky 비밀번호 변경 이메일 인증");
        String msgg="";
        msgg+="인증번호는 "+randomNumber+" 입니다.";
        message.setText(msgg,"utf-8","html");
        message.setFrom(new InternetAddress("linky_mail@naver.com","linky_authentication"));

        return message;
    }

    public void sendSimplePasswordChangeMessage(String to, int randomNumber){
        try{
            MimeMessage message=passwordChangeMessage(to, randomNumber);
            emailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendSimpleMessage(String to,String id){
        try{
            MimeMessage message=createMessage(to, id);
            emailSender.send(message);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }


}
