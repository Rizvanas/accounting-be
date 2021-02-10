package com.rizvanchalilovas.accountingbe.dtos.transaction.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.TransactionType;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class TransactionUpdateRequest {
    private JsonNullable<String> title = JsonNullable.undefined();

    private JsonNullable<String> comment = JsonNullable.undefined();

    private JsonNullable<Long> moneyAmount = JsonNullable.undefined();

    private JsonNullable<TransactionType> type = JsonNullable.undefined();
}
