package com.another.ticket.service;

import com.another.ticket.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

public class TaskSpecifications {

    public static Specification<Task> hasStatusIn(List<String> status) {
        return (root, query, criteriaBuilder)
                -> root.get("status").in(status);
    }

    public static Specification<Task> hasPriorityIn(List<String> priority) {
        return (root, query, criteriaBuilder)
                -> root.get("priority").in(priority);
    }

    public static Specification<Task> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("users").get("username"), username);
    }

    public static Specification<Task> hasDateRange(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createDate"), startDate, endDate);
    }
}
