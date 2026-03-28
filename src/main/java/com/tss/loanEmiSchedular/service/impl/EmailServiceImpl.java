package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();

        try{

            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);

            javaMailSender.send(mimeMessage);

        }
        catch (Exception e){
            throw new RuntimeException("Email sending failed",e);
        }
    }
}
