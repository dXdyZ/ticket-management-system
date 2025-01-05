package com.another.reportservice.repository;

import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByCreateDateBetween(LocalDate createDateAfter, LocalDate createDateBefore);
    List<Task> findAllByUsers(Users users);

    @Query(value = "SELECT EXTRACT(MONTH FROM create_date) AS month, COUNT(*) AS count " +
            "FROM task " +
            "WHERE create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY month " +
            "ORDER BY month",
            nativeQuery = true)
    List<Object[]> findTaskCountsByMonth(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // Группировка по приоритету
    @Query(value = "SELECT priority, COUNT(*) AS count " +
            "FROM task " +
            "WHERE create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY priority",
            nativeQuery = true)
    List<Object[]> findTaskCountsByPriority(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    // Группировка по статусу
    @Query(value = "SELECT status, COUNT(*) AS count " +
            "FROM task " +
            "WHERE create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY status",
            nativeQuery = true)
    List<Object[]> findTaskCountsByStatus(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
