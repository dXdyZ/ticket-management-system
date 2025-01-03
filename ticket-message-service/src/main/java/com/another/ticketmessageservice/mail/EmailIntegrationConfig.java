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

    public void sendTaskMessage(Task task, String recipientEmail) throws MessagingException {
        //1. Create mimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        //2. Тема, от кого, кому
        mimeMessageHelper.setSubject("Бронирование задачи");
        mimeMessageHelper.setTo(recipientEmail);
        mimeMessageHelper.setFrom(fromMail);

        log.info("task id: {}", task.getId());

        //3. Заполняем Thymeleaf Context
        Context context = new Context();
        context.setVariable("task", task);
        String link = "http://localhost:9191/conf-task/" + task.getId();
        log.info("link: {}", link);
        context.setVariable("taskLink", link);

        // 4. Генерим HTML из шаблона emailTaskTemplate.html
        String htmlBody = templateEngine.process("emailTaskTemplate.html", context);

        //5. Устанавливаем HTML-body
        mimeMessageHelper.setText(htmlBody, true);

        //Send mail
        javaMailSender.send(mimeMessage);
    }
}








