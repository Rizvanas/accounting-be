package com.rizvanchalilovas.accountingbe.dtos.transaction.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.Transaction;
import com.rizvanchalilovas.accountingbe.models.TransactionType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionResponse {
    private Long id;
    private String title;
    private String comment;
    private Long moneyAmount;
    private TransactionType type;
    private Long categoryId;

    public static TransactionResponse fromTransaction(Transaction t) {
        return builder()
                .id(t.getId())
                .title(t.getTitle())
                .comment(t.getComment())
                .moneyAmount(t.getMoneyAmount())
                .type(t.getType())
                .categoryId(t.getCategory().getId())
                .build();
    }
}
