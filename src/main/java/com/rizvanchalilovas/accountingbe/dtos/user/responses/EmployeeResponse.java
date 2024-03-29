package com.rizvanchalilovas.accountingbe.dtos.user.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.CompanyEmployee;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmployeeResponse {
    private Long id;
    private String fullName;
    private String email;
    private RoleEnum role;
    private Long companyId;

    public static EmployeeResponse fromEmployee(CompanyEmployee employee) {
        return builder()
                .id(employee.getId())
                .fullName(employee.getUser().getFullName())
                .email(employee.getUser().getEmail())
                .role(employee.getRole().getName())
                .companyId(employee.getCompany().getId())
                .build();
    }
}
