package com.another.ticketmessageservice.repository;

import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class CustomStatusLogRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomStatusLogRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void addedDateUpdateStatus(String id, String fieldName, LocalDateTime date) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().push(fieldName).each(date);
        mongoTemplate.updateFirst(query, update, StatusLog.class);
    }
}
