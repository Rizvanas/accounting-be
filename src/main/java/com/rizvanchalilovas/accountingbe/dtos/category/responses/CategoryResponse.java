package com.rizvanchalilovas.accountingbe.dtos.category.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.transaction.responses.TransactionResponse;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.models.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryResponse {
    private Long id;
    private String title;
    private String description;
    private EmployeeResponse responsibleEmployee;
    private Long totalIncome;
    private Long totalExpenditure;
    private List<TransactionResponse> transactions;
    private List<CategoryResponse> subCategories;
    private Long parentId;
    private Long companyId;

    public static CategoryResponse fromCategory(Category c) {
        Long parentId = c.getParent() == null ? null : c.getParent().getId();
        return builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .responsibleEmployee(EmployeeResponse.fromEmployee(c.getManager()))
                .totalIncome(c.getTotalIncome())
                .totalExpenditure(c.getTotalExpenditure())
                .transactions(c.getTransactions().stream()
                        .map(TransactionResponse::fromTransaction)
                        .collect(Collectors.toList()))
                .subCategories(c.getSubCategories().stream()
                        .map(CategoryResponse::fromCategory)
                        .collect(Collectors.toList()))
                .parentId(parentId)
                .companyId(c.getCompany().getId())
                .build();
    }
}
