/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package nrls.adapter.services;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import nrls.adapter.enums.RequestType;
import nrls.adapter.model.ErrorInstance;
import nrls.adapter.model.ErrorReport;
import nrls.adapter.model.Report;

@Component
public class EmailService {

	@Autowired
	private LoggingService loggingService;
    
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
        	e.printStackTrace();
            loggingService.error("Error sending Report email", RequestType.PROVIDER);
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
            loggingService.error("Error sending Error email", RequestType.PROVIDER);
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
                    loggingService.error("Error waiting for interval before sending email again", RequestType.PROVIDER);
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
