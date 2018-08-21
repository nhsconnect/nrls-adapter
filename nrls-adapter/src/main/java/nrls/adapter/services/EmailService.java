package nrls.adapter.services;

import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.MimeMessage;
import nrls.adapter.model.ErrorInstance;
import nrls.adapter.model.ErrorReport;
import nrls.adapter.model.Report;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private static final Logger LOG = Logger.getLogger(EmailService.class);
    
    @Value("${batch.report.recipient.email}")
    private String reportRecipient;

    @Value("${error.report.recipient.email}")
    private String errorReportRecipient;

    @Value("${error.report.interval.mins}")
    private int errorEmailInterval;

    @Autowired
    private JavaMailSender javaMailSender;

    private static List<ErrorInstance> errors;
    private static boolean waitingAfterErrorEmailSent;

    public EmailService() {
        waitingAfterErrorEmailSent = false;
        errors = new ArrayList<>();
    }

    public void sendReport(Report report) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(reportRecipient);
            helper.setSubject("NRLS Adapter - Report");
            helper.setText(report.getReportAsHTML(), true);
            javaMailSender.send(mail);
        } catch (Exception e) {
            LOG.error("Error sending Report email");
        }
    }

    public void sendError(ErrorInstance message) {
        errors.add(message);
        if (!waitingAfterErrorEmailSent) {
            sendErrorEmail();
        }
    }

    private void sendErrorEmail() {
        waitingAfterErrorEmailSent = true;  // Change to waiting so another attempt to send is not made before the timeout

        // Store a copy and clear the error list
        List<ErrorInstance> errorsToSend = new ArrayList<>(errors);
        errors.clear();

        // send email
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(errorReportRecipient);
            helper.setSubject("NRLS Adapter - Errors");
            ErrorReport errorReport = new ErrorReport(errorsToSend);
            helper.setText(errorReport.getReportAsHTML(), true);
            javaMailSender.send(mail);
        } catch (Exception e) {
            LOG.error("Error sending Error email");
        }

        scheduleTaskWithFixedDelay();
    }

    private void scheduleTaskWithFixedDelay() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(errorEmailInterval * 60000); // 60000 milliseconds in 1 minute
                } catch (InterruptedException ex) {
                    LOG.error("Error waiting for interval before sending email again");
                }
                // Check if there are further errors built up, if so send them and start another wait to check again at the next interval
                if (errors.size() > 0) {
                    sendErrorEmail();
                } else {
                    waitingAfterErrorEmailSent = false; // Allow the next error to imidiatly trigger the email
                }
            }
        }.start();
    }

}
