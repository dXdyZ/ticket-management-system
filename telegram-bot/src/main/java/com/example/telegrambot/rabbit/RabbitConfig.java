package com.example.telegrambot.rabbit;

import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(new ObjectMapper().findAndRegisterModules());
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setIdClassMapping(Map.of(
                "com.another.ticket.entity.Task", Task.class,
                "com.another.ticket.entity.Users", Users.class
        ));
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
