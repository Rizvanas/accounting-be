package com.rizvanchalilovas.accountingbe.dtos.company.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyAdditionRequest {
    @NotNull
    private String username;

    @NotNull(message = "field name cannot be null.")
    @Size(min = 3, max = 255, message = "Company name length must be between 3 and 255 characters")
    private String companyName;

    private String description;
}
