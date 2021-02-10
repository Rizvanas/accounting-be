package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
    @JsonProperty("income")
    INCOME,

    @JsonProperty("expenditure")
    EXPENDITURE
}
