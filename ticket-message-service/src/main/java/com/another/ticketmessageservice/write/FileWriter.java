package com.another.ticketmessageservice.write;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableIntegration
public class FileWriter {

    /**
     * Канал для отправки сообщений чтобы они записывались в файл
     */
    @Bean
    public MessageChannel setStatusFiletWriteChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlow
                .from(setStatusFiletWriteChannel())
                .transform(Transformers.toJson())
                .handle(Files.outboundAdapter(new File("/home/another/dev/developering/ticket-management-system/log"))
                        .autoCreateDirectory(true)
                        .fileNameExpression("headers['file_name'] ?: 'default.log'")
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true)
                )
                .get();
    }
}











