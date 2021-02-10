package com.rizvanchalilovas.accountingbe.dtos.company.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyUpdateRequest {
    private String name;
    private String description;
    private String ownerUsername;
}
