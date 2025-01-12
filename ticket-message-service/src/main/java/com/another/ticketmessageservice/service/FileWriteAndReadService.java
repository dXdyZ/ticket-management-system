package com.another.ticketmessageservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class FileWriteAndReadService {
    private final MessageChannel setStatusFiletWriteChannel;

    public FileWriteAndReadService(@Qualifier("setStatusFiletWriteChannel") MessageChannel setStatusFiletWriteChannel) {
        this.setStatusFiletWriteChannel = setStatusFiletWriteChannel;
    }

    public void writeFileData(Object task, String fileName) {
        Message<Object> message = MessageBuilder.withPayload(task)
                .setHeader("file_name", fileName)
                .build();
        setStatusFiletWriteChannel.send(message);
    }

    public File fileRead(String pathToFile) throws FileNotFoundException {
        File file = new File(pathToFile);
        if (file.exists()) {
            return file;
        } else {
            writeFileData("file not found: " + pathToFile, "get_report_file.log");
            throw new FileNotFoundException();
        }
    }
}
