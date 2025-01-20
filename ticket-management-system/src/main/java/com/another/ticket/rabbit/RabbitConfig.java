package com.another.ticket.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${queue.name.emailGetTaskInWork}")
    private String emailGetTaskInWork;

    @Value("${queue.name.SendBotMessage}")
    private String botMessage;

    @Value("${queue.name.CreateTask}")
    private String createTask;

    @Value("${queue.name.SetStatusTask}")
    private String setStatusTask;

    @Value("${queue.name.GetStatusLog}")
    private String getStatusLogData;

    @Value("${queue.name.SendMailReport}")
    private String sendMailReport;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Bean
    public Queue sendMailGetTaskInWork() {
        return new Queue(emailGetTaskInWork, false);
    }

    @Bean
    public Queue sendMailCreateTask() {
        return new Queue(createTask, false);
    }

    @Bean
    public Queue sendBotMessage() {
        return new Queue(botMessage, false);
    }

    @Bean
    public Queue sendMailSetStatusTask() {
        return new Queue(setStatusTask, false);
    }

    @Bean
    public Queue getStatusLogQueue() {
        return new Queue(getStatusLogData, false);
    }

    @Bean
    public Queue sendMailReport() {
        return new Queue(sendMailReport, false);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}








