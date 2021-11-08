package com.rizvanchalilovas.accountingbe.dtos.authentication.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthenticationRequest {
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
