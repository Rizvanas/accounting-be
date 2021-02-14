package com.rizvanchalilovas.accountingbe.dtos.category.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryAdditionRequest {
    @Length(min = 3, max = 50)
    @NotBlank
    @NotEmpty
    private String title;

    @Length(max = 255)
    @NotBlank
    private JsonNullable<String> description = JsonNullable.undefined();

    @NotNull
    private Long managingEmployeeId;

    private List<TransactionAdditionRequest> transactions = new ArrayList<>();

    private Long parentId;
}
