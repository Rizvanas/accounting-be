package com.rizvanchalilovas.accountingbe.dtos.category.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryAdditionRequest {
    @NotBlank
    @Length(min = 3, max = 50)
    private String title;

    @Length(max = 255)
    private String description;

    @NotNull
    private Long managingEmployeeId;

    private List<TransactionAdditionRequest> transactions = new ArrayList<>();

    private Long parentId;
}
