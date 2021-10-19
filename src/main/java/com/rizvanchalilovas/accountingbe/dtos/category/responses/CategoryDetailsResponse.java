package com.rizvanchalilovas.accountingbe.dtos.category.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.transaction.responses.TransactionResponse;
import com.rizvanchalilovas.accountingbe.models.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryDetailsResponse {
    private Long id;
    private String title;
    private String description;
    private Long responsibleEmployeeId;
    private String responsibleEmployeeFullName;
    private Long totalIncome;
    private Long totalExpenditure;
    private List<TransactionResponse> transactions;
    private Category parent;
    private List<CategoryResponse> subCategories;
    private Long companyId;

    public static CategoryDetailsResponse toCategoryDetailsResponse(Category c) {
        return builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .responsibleEmployeeId(c.getManager().getId())
                .responsibleEmployeeFullName(c.getManager().getUser().getFullName())
                .totalIncome(c.getTotalIncome())
                .totalExpenditure(c.getTotalExpenditure())
                .transactions(c.getTransactions().stream()
                        .map(TransactionResponse::fromTransaction)
                        .collect(Collectors.toList()))
                .parent(c.getParent())
                .subCategories(c.getSubCategories().stream()
                        .map(CategoryResponse::fromCategory)
                        .collect(Collectors.toList()))
                .companyId(c.getCompany().getId())
                .build();
    }
}
