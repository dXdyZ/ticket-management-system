package com.another.ticketmessageservice.repository;

import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatusLogRepository extends MongoRepository<StatusLog, String> {
    Optional<StatusLog> findByTask_Id(Long taskId);
    List<StatusLog> findAllBySetStatusOPENBetween(LocalDateTime setStatusOPENAfter, LocalDateTime setStatusOPENBefore);
}
