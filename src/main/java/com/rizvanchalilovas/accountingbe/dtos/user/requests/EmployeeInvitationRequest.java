package com.rizvanchalilovas.accountingbe.dtos.user.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class EmployeeInvitationRequest {
    @NotNull
    String username;
    boolean isGuest;
}
