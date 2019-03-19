package oktenweb.restaurantbackend1.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//    add propMail to application.properties

@PropertySource("classpath:application.properties")
@Service
public class EmailService {

    @Autowired
    Environment env;

    @Autowired
    private JavaMailSender javaMailSender;



    public void sendEmail(String email
           // ,MultipartFile file
    ) throws MessagingException {
//         String tag = "<form th:action=\"@{/confirm}\" method=\"get\">" +
//                "<input type=\"submit\" />" +
//                "</form>";
        String tag1 = "<div>\n" +
                "    <a href=\"http://localhost:8080/confirm\" target=\"_blank\"> Confirm </a>\n" +
                "</div>";


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username")));
            helper.setTo(email);
            helper.setText(tag1, true);

           // helper.addAttachment(file.getOriginalFilename(), file);
            helper.setSubject("CONFIRMATION MESSAGE");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);

    }
}