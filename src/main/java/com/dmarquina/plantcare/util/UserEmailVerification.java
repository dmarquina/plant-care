package com.dmarquina.plantcare.util;

import com.dmarquina.plantcare.model.User;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class UserEmailVerification {

  @Autowired
  private JavaMailSender javaMailSender;

  public String generateVerificationCode() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 6;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString()
        .toUpperCase();
  }

  public void sendVerificationCodeMail(User user) {
    String subject = "Verificación de correo para Akira's Garden App";
    StringBuilder message = new StringBuilder();
    message.append("<html><body style=\"background: #4DB6AC;text-align:center;color:#ffffff;font-size:20px;\">");
    message.append("<h2>HOLA ").append(user.getDisplayName().toUpperCase()).append("</h2>");
    message.append("<h3>EL CODIGO PARA VERIFICAR TU CORREO ES</h3>");
    message.append("<h1 style=\"font-size: 72px;margin-top:0px\">").append(user.getVerificationCode()).append("<h1>");
    message.append("</body></html>");
    sendEmail(user.getEmail(), subject, message.toString());
  }

  private void sendEmail(String to, String subject, String message) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setFrom(new InternetAddress("akiras.garden@gmail.com", "Akira's Garden"));
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(message, true); // Use this or above line.
      Thread newThread = new Thread(() -> javaMailSender.send(mimeMessage));
      newThread.start();
    } catch (MessagingException me) {
      me.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } finally {
      System.out.println("Correo enviado");
    }
  }
}
