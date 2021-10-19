package com.rizvanchalilovas.accountingbe.dtos.authentication.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthenticationResponse {
    private UserResponse user;
    private String jwtToken;

    public static AuthenticationResponse fromUserResponse(UserResponse ur, String jwtToken) {
        return builder()
                .user(ur)
                .jwtToken(jwtToken)
                .build();
    }
}
