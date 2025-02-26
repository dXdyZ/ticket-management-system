package com.another.reportservice.rabbit;

import com.another.reportservice.custom_exception.FutureDateException;
import com.another.reportservice.entity.RequestReportDTO;
import com.another.reportservice.service.reportService.ReportTaskService;
import com.another.reportservice.service.reportService.ReportUserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Receiver {
    private final ReportTaskService reportTaskService;
    private final ReportUserService reportUserService;
    private final MessageConverter messageConverter;


    @Autowired
    public Receiver(ReportTaskService reportTaskService, ReportUserService reportUserService,
                    MessageConverter messageConverter) {
        this.reportTaskService = reportTaskService;
        this.reportUserService = reportUserService;
        this.messageConverter = messageConverter;
    }

    @RabbitListener(queues = "MessageRequestReport")
    public void receiveRequestReport(Message message) throws FutureDateException {
        MessageProperties properties = message.getMessageProperties();
        String reportType = properties.getHeader("REPORT_TYPE");
        RequestReportDTO requestReportDTO = (RequestReportDTO) messageConverter.fromMessage(message);
        switch (reportType) {
            case "task_processing" -> {
                reportTaskService.getAndSendProcessingTaskReport(
                        requestReportDTO.getStart(), requestReportDTO.getEnd(), requestReportDTO.getEmail()
                );
            }
            case "task_period" -> {
                reportTaskService.getReportNumberOfCreateTaskPeriod(
                        requestReportDTO.getStart(), requestReportDTO.getEnd(), requestReportDTO.getUsername(),
                        requestReportDTO.getEmail()
                );
            }
            case "user_efficiency" -> {
                reportUserService.getReportPerformerEfficiencyUser(
                        requestReportDTO.getUsername(), requestReportDTO.getEmail()
                );
            }
            case "user_period" -> {
                reportUserService.getReportNumberOfRegisterUserPeriod(
                        requestReportDTO.getStart(), requestReportDTO.getEnd(), requestReportDTO.getEmail()
                );
            }1
        }
    }
}
