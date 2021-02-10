package com.rizvanchalilovas.accountingbe.dtos.category.requests;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryUpdateRequest {
    @NotNull
    private JsonNullable<String> title = JsonNullable.undefined();

    @NotNull
    private JsonNullable<String> description = JsonNullable.undefined();

    @NotNull
    private JsonNullable<Long> managingEmployeeId = JsonNullable.undefined();

    private JsonNullable<Long> parentId = JsonNullable.undefined();
}
