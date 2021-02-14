package com.rizvanchalilovas.accountingbe.dtos.transaction.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.TransactionType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class TransactionUpdateRequest {
    @NotBlank
    @Length(min = 3, max = 255)
    private JsonNullable<String> title = JsonNullable.undefined();

    @NotBlank
    @Length(min = 3, max = 255)
    private JsonNullable<String> comment = JsonNullable.undefined();

    @NotNull
    @Positive
    private JsonNullable<Long> moneyAmount = JsonNullable.undefined();

    @NotNull
    private JsonNullable<TransactionType> type = JsonNullable.undefined();
}
