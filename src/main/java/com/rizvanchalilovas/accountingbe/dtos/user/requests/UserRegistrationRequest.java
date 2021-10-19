package com.rizvanchalilovas.accountingbe.dtos.user.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class UserRegistrationRequest {
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 50)
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 255)
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 100)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 100)
    private String lastName;
}
