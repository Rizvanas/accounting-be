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
    @NotBlank(message = "[title]: is required")
    @Size(min = 3, max = 255, message = "[title]: must be between 3 and 255 characters long.")
    private String title;

    @Size(min = 3, max = 255, message = "[comment]: must be between 3 and 255 characters long.")
    private String comment;

    @NotNull(message = "[money_amount]: cannot be null")
    @PositiveOrZero(message = "[money_amount]: value must be greater than 0.")
    private Long moneyAmount;

    @NotNull
    private TransactionType type;
}
