package com.another.ticket.repository;

import com.another.ticket.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByUsers_Id(Long usersId);
}
