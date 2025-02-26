package com.zerobase.fastlms.compoents;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailCompoents {
    private final JavaMailSender javaMailSender;

    public void sendMailTest(){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("email@example.com");
        msg.setSubject("Test");
        msg.setText("This is a test");
        javaMailSender.send(msg);
    }

    public boolean sendMail(String mail, String subject, String text){
        boolean result = false;
        MimeMessagePreparator msg = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };
        try {
            javaMailSender.send(msg);
            result = true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }
}
