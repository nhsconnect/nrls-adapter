package nrls.adapter.services;

import javax.mail.internet.MimeMessage;
import nrls.adapter.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    
    @Value("${batch.report.recipient.email}")
    private String reportRecipient;
    
    @Autowired  
    private JavaMailSender javaMailSender;
 
    public void sendReport(Report report) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(reportRecipient);
            helper.setSubject("NRLS Adapter - Report");
            helper.setText(report.getReportAsHTML(), true);
            javaMailSender.send(mail);
        } catch (Exception e) {
            System.err.println("Error sending report email");
        }
    }
    
}
