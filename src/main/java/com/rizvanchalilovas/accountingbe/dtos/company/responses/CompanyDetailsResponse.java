package com.rizvanchalilovas.accountingbe.dtos.company.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryResponse;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.models.PermissionEnum;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerUsername;
    private String ownerEmail;
    private String ownerFullName;
    private Long totalIncome;
    private Long totalExpenditure;
    private Set<PermissionEnum> currentUserPermissions;
    private List<EmployeeResponse> employees;
    private List<CategoryResponse> categories;

    public static CompanyDetailsResponse fromCompany(Company c, String loggedInEmail) {
        var companyCeo = c.getEmployees().stream()
                .filter(emp -> emp.getRole().getName() == RoleEnum.CEO)
                .findFirst()
                .get().getUser();

        var loggedInRole = c.getEmployees().stream()
                .filter(emp -> emp.getUser().getEmail().equals(loggedInEmail))
                .findFirst()
                .get().getRole();

        return builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .ownerUsername(companyCeo.getUsername())
                .ownerFullName(companyCeo.getFullName())
                .ownerEmail(companyCeo.getEmail())
                .totalIncome(c.getTotalIncome())
                .currentUserPermissions(loggedInRole.getPermissions().stream()
                        .map(PermissionEnum::fromPermission)
                        .collect(Collectors.toSet()))
                .totalExpenditure(c.getTotalExpenditure())
                .employees(c.getEmployees().stream()
                        .map(EmployeeResponse::fromEmployee)
                        .collect(Collectors.toList()))
                .categories(c.getCategories().stream()
                        .map(CategoryResponse::fromCategory)
                        .collect(Collectors.toList()))
                .build();
    }
}
