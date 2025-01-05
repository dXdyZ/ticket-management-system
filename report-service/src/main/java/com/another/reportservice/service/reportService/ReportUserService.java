package com.another.reportservice.service.reportService;


import com.another.reportservice.entity.Role;
import com.another.reportservice.entity.Users;
import com.another.reportservice.entity.reportEntity.UserPeriodReportEntity;
import com.another.reportservice.service.repositoryService.MapDate;
import com.another.reportservice.service.repositoryService.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ReportUserService {
    private final UserService userService;

    public ReportUserService(UserService userService) {
        this.userService = userService;
    }

    public UserPeriodReportEntity getReportNumberOfRegisterUserPeriod(String start, String end) throws ParseException, ExecutionException, InterruptedException {
        List<LocalDate> dates = MapDate.mapDate(start, end);
        List<Users> users = userService.getUserBetweenDate(dates.get(0), dates.get(1));
        return UserPeriodReportEntity.builder()
                .quantityUsers(Long.valueOf(users.size() + 1))
                .registerUserByMonth(partitionUserByMoth(users).get())
                .registerUserByRoleMonth(partitionUserByRole(users).get())
                .build();
    }

    @Async
    public Future<Map<Month, Long>> partitionUserByMoth(List<Users> users) {
        EnumMap<Month, Long> quantityUserByMonth = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            quantityUserByMonth.put(month, 0L);
        }
        for (Users user : users) {
            Month month = user.getMontyEnum();
            quantityUserByMonth.merge(month, 1L, Long::sum);
        }
        return AsyncResult.forValue(quantityUserByMonth);
    }

    @Async
    public Future<Map<Role, Long>> partitionUserByRole(List<Users> users) {
        EnumMap<Role, Long> registerUserByRoleMonth = new EnumMap<>(Role.class);
        registerUserByRoleMonth.put(Role.ROLE_CLIENT, 0L);
        registerUserByRoleMonth.put(Role.ROLE_PERFORMER, 0L);
        registerUserByRoleMonth.put(Role.ROLE_ADMIN, 0L);
        for (Users user : users) {
            Role role = user.getRole();
            registerUserByRoleMonth.put(role, registerUserByRoleMonth.get(role) + 1);
        }
        return AsyncResult.forValue(registerUserByRoleMonth);
    }
}









