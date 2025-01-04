package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.entity.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class FileWriteService {
    private final MessageChannel setStatusFiletWriteChannel;

    public FileWriteService(@Qualifier("setStatusFiletWriteChannel") MessageChannel setStatusFiletWriteChannel) {
        this.setStatusFiletWriteChannel = setStatusFiletWriteChannel;
    }

    public void writeFileData(Task task, String fileName) {
        Message<Task> message = MessageBuilder.withPayload(task)
                .setHeader("file_name", fileName)
                .build();
        setStatusFiletWriteChannel.send(message);
    }
}
