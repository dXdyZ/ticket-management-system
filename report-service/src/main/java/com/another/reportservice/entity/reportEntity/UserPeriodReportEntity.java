package com.another.reportservice.entity.reportEntity;

import com.another.reportservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPeriodReportEntity {
    private Long quantityUsers;
    private Map<Month, Long> registerUserByMonth;
    private Map<Role, Long> registerUserByRoleMonth;
}
