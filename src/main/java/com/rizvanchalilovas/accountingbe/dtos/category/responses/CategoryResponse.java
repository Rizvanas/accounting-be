package com.rizvanchalilovas.accountingbe.dtos.category.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.models.Category;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryResponse {
    private Long id;
    private String title;
    private EmployeeResponse responsibleEmployee;
    private Long totalIncome;
    private Long totalExpenditure;
    private Category parent;
    private Set<CategoryResponse> subCategories;
    private Long companyId;

    public static CategoryResponse fromCategory(Category c) {
        return builder()
                .id(c.getId())
                .title(c.getTitle())
                .responsibleEmployee(EmployeeResponse.fromEmployee(c.getManager()))
                .totalIncome(c.getTotalIncome())
                .totalExpenditure(c.getTotalExpenditure())
                .parent(c.getParent())
                .subCategories(c.getSubCategories().stream()
                        .map(CategoryResponse::fromCategory)
                        .collect(Collectors.toSet()))
                .companyId(c.getCompany().getId())
                .build();
    }
}
