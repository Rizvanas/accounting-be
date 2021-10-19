package com.rizvanchalilovas.accountingbe.dtos.company.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyUpdateRequest {
    @NotBlank
    @Length(min = 3, max = 255)
    private JsonNullable<String> name = JsonNullable.undefined();

    @NotBlank
    @Length(min = 3, max = 255)
    private JsonNullable<String> description = JsonNullable.undefined();
}
