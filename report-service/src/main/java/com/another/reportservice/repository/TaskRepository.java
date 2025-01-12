package com.another.reportservice.repository;

import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByCreateDateBetween(LocalDateTime createDateAfter, LocalDateTime createDateBefore);
    List<Task> findAllByUsersAndCreateDateBetween(Users users, LocalDateTime createDateAfter, LocalDateTime createDateBefore);
    List<Task> findAllByStatusAndUsers_Username(Status status, String usersUsername);
    List<Task> findAllByUsers_Username(String usersUsername);
}
