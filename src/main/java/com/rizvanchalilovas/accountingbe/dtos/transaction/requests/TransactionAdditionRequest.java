package com.rizvanchalilovas.accountingbe.dtos.transaction.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.TransactionType;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class TransactionAdditionRequest {
    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @Size(min = 3, max = 255)
    private String comment;

    @NotNull
    @Positive
    private Long moneyAmount;

    @NotNull
    private TransactionType type;
}
