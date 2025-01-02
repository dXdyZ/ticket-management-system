package com.another.ticket.repository;

import com.another.ticket.entity.Priority;
import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByUsers_Id(Long usersId);
    List<Task> findAllByStatusIn(Collection<Status> statuses, Pageable pageable);
    List<Task> findAllByPriorityIn(Collection<Priority> priorities, Pageable pageable);
    List<Task> findAllByUsers_Username(String usersUsername, Pageable pageable);
    List<Task> findAllByCreateDateBetween(Date createDateAfter, Date createDateBefore, Pageable pageable);
}
