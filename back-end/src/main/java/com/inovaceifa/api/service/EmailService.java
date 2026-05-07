package com.inovaceifa.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCredenciais(String email, String senha) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Acesso ao Sistema InovaCeifa");
        message.setText(
                "Olá!\n\n" +
                        "Seu acesso ao sistema foi criado com sucesso.\n\n" +
                        "Login: " + email + "\n" +
                        "Senha: " + senha + "\n\n" +
                        "Recomendamos alterar sua senha no primeiro acesso.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe InovaCeifa"
        );

        mailSender.send(message);
    }
}
