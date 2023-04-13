package fpt.code.mail;

import fpt.code.entities.User;
import fpt.code.repository.UserRepository;
import fpt.code.security.service.UserDetailsImpl;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private JavaMailSender mailSender;
    public boolean verify(String verificationCode) {
        User user = repo.findByVerificationCode(verificationCode);
        if (user == null || user.isEnable()  ) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnable(true);
            repo.save(user);
            return true;
        }

    }


    public void register(User user) throws  MessagingException {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnable(false);
        repo.save(user);
        sendVerificationEmail(user);

    }

    public void sendVerificationEmail(User user) {
        String toAddress = user.getEmail();
        String fromAddress = "nguyenhuuminhyl@gmail.com";
        String senderName = "Hữu Minh";
        String subject = "Please verify your registration";
        String body = "Dear [[name]],<br>"
                + "Đây là mã xác thực của bạn:<br>"
                + "<h3>[[code]]</h3>"
                + "Copy lại mã này rồi gửi cho a nhé :<br>"
                + "Thank you<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            body = body.replace("[[name]]", user.getUsername());
            body = body.replace("[[code]]", user.getVerificationCode());
            helper.setText(body, true);
            mailSender.send(message);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
