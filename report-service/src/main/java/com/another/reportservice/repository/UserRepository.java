package com.another.reportservice.repository;

import com.another.reportservice.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    List<Users> findAllByCreateDataBetween(LocalDate start, LocalDate date);
    Optional<Users> findByUsername(String username);
}
