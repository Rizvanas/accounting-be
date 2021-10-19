package com.rizvanchalilovas.accountingbe.dtos.company.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyListItem {
    private Long id;
    private String name;
    private String description;
    private String ownerUsername;
    private String ownerFullName;
    private Long totalIncome;
    private Long totalExpenditure;
    private Integer employeeCount;
    private boolean ownedByCurrentUser;

    public static CompanyListItem fromCompany(Company company, String loggedInEmail) {
        var companyCeo = company.getEmployees().stream()
                .filter(emp -> emp.getRole().getName() == RoleEnum.CEO)
                .findFirst()
                .get().getUser();

        return builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .ownerUsername(companyCeo.getUsername())
                .ownerFullName(companyCeo.getFullName())
                .totalIncome(company.getTotalIncome())
                .totalExpenditure(company.getTotalExpenditure())
                .employeeCount(company.getEmployees().size())
                .ownedByCurrentUser(companyCeo.getEmail().equals(loggedInEmail))
                .build();
    }
}
