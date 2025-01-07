package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.component.MapingDate;
import com.another.ticketmessageservice.entity.Status;
import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import com.another.ticketmessageservice.rabbit.RabbitSenderMessage;
import com.another.ticketmessageservice.repository.CustomStatusLogRepository;
import com.another.ticketmessageservice.repository.StatusLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StatusLogService {
    private final StatusLogRepository statusLogRepository;
    private final CustomStatusLogRepository customStatusLogRepository;

    @Autowired
    public StatusLogService(StatusLogRepository statusLogRepository,
                            CustomStatusLogRepository customStatusLogRepository) {
        this.statusLogRepository = statusLogRepository;
        this.customStatusLogRepository = customStatusLogRepository;
    }

    public List<StatusLog> getStatusLogByTaskTimeProcessing(String start, String end) {
        List<LocalDateTime> localDateTimes = MapingDate.getLocalDateFromString(start, end);
        return statusLogRepository.findAllBySetStatusOPENBetween(localDateTimes.get(0), localDateTimes.get(1));
    }

    public void saveOrUpdateStatusLog(Task task) {
        Optional<StatusLog> statusLog = statusLogRepository.findByTask_Id(task.getId());
        if (statusLog.isEmpty()) {
            StatusLog setStatusLog = new StatusLog();
            setStatusLog.setTask(task);
            if (task.getStatus().equals(Status.CLOSED)) {
                setStatusLog.setSetStatusCLOSED(LocalDateTime.now());
            }
            if (task.getStatus().equals(Status.OPEN)) {
                setStatusLog.setSetStatusOPEN(LocalDateTime.now());
            }
            if (task.getStatus().equals(Status.IN_JOB)) {
                setStatusLog.setSetStatusIN_JOB(List.of(LocalDateTime.now()));
            }
            if (task.getStatus().equals(Status.AWAITING_RESPONSE)) {
                setStatusLog.setSetStatusAWATIN_RESPONSE(List.of(LocalDateTime.now()));
            }
            statusLogRepository.save(setStatusLog);
        } else {
            if (task.getStatus().equals(Status.CLOSED)) {
                customStatusLogRepository.addedDateUpdateStatus(statusLog.get().getId(), "setStatusCLOSED", LocalDateTime.now());
            }
            if (task.getStatus().equals(Status.OPEN)) {
                customStatusLogRepository.addedDateUpdateStatus(statusLog.get().getId(), "setStatusOPEN", LocalDateTime.now());
            }
            if (task.getStatus().equals(Status.IN_JOB)) {
                customStatusLogRepository.addedDateUpdateStatus(statusLog.get().getId(), "setStatusIN_JOB", LocalDateTime.now());
            }
            if (task.getStatus().equals(Status.AWAITING_RESPONSE)) {
                customStatusLogRepository.addedDateUpdateStatus(statusLog.get().getId(), "setStatusAWATIN_RESPONSE", LocalDateTime.now());
            }
        }
    }
}
