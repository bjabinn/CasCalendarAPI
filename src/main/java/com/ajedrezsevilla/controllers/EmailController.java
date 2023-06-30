package com.ajedrezsevilla.controllers;

import com.ajedrezsevilla.models.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("email")
public class EmailController {

    //Configuración https://howtodoinjava.com/spring-boot2/send-email-with-attachment/
    private final JavaMailSender mailSender;

    @PostMapping()
    public void sendEmailNotification (@RequestBody EmailRequest request){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bjabinn@hotmail.com");
        message.setTo(request.getEmail());
        message.setSubject("TEST");
        message.setText(request.getContent());

        mailSender.send(message);
    }
}
