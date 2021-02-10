package com.rizvanchalilovas.accountingbe.dtos.user.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rizvanchalilovas.accountingbe.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;

    public User toUser() {
        var user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

    public static UserResponse fromUser(User u) {
        return builder()
                .id(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .build();
    }
}
