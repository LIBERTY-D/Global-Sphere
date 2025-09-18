package com.daniel.app.global.sphere.services;

import com.daniel.app.global.sphere.environment.MailEnv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    public static final String UTF_8 = "UTF-8";
    public static final String EMAIL_FAILURE_MESSAGE = "EMAIL FAILURE";
    private  final JavaMailSender javaMailSender;
    private final MailEnv mailEnv;
    private final ITemplateEngine iTemplateEngine;


    @Async
    public void sendEmail(Map<String, Object> payload, String template) throws MessagingException {
            Context context =  new Context();
            context.setVariables(payload);

            String htmlContent =  iTemplateEngine.process(template,context);
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true, UTF_8);
            mimeMessageHelper.setSubject((String) payload.get("subject"));
            mimeMessageHelper.setFrom(mailEnv.getUsername());
            mimeMessageHelper.setTo((String) payload.get("to"));
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);


    }

    private  MimeMessage getMimeMessage(){
        return  javaMailSender.createMimeMessage();
    }
}