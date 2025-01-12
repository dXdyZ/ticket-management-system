package com.another.ticketmessageservice.mail;

import com.another.ticketmessageservice.entity.Task;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

@Slf4j
@Service
public class EmailIntegrationConfig {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    public EmailIntegrationConfig(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }


    public void sendReport(File report, String email, String topic) {
        createEmailMessage(null, email, topic, null, report);
    }

    public void sendBugReportForUser(String userEmail) {
        createEmailMessage(new Context(), userEmail, "Ошибка во время создания отчета", "bug_report.html", null);
    }

    public void sendTaskMessage(Task task, String recipientEmail) {
        Context context = new Context();
        context.setVariable("task", task);
        String link = "http://localhost:9191/conf-task/" + task.getId();
        context.setVariable("taskLink", link);
        createEmailMessage(context, recipientEmail, "Бронирование задачи", "emailTaskTemplate.html", null);

    }

    private void createEmailMessage(Context context, String email, String topic, String htmlFileName, File file)  {
        //1. Create mimeMessage
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            //2. Тема, от кого, кому
            mimeMessageHelper.setSubject(topic);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(fromMail);


            if (file == null) {
                String htmlBody = templateEngine.process(htmlFileName, context);
                //5. Устанавливаем HTML-body
                mimeMessageHelper.setText(htmlBody, true);
                //Send mail
                javaMailSender.send(mimeMessage);
            } else {
                mimeMessageHelper.setText(topic);
                mimeMessageHelper.addAttachment(file.getName(), file);
                javaMailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}








