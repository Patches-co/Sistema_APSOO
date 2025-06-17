/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.service;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
/**
 *
 * @author Jvvpa
 */
public class EmailService {
    private static final String REMETENTE_EMAIL = "software.ezplace@gmail.com";
    private static final String REMETENTE_SENHA = "zgir pdcx nwdn djrd";
    
    public static void enviarEmail(String destinatario, String assunto, String corpo) {
        System.out.println("Preparando para enviar email para: " + destinatario);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE_EMAIL, REMETENTE_SENHA);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMETENTE_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);

            System.out.println("E-mail enviado com sucesso!");

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar o e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
