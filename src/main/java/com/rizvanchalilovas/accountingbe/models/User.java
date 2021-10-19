package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "username")
    @Length(min = 3, max = 50)
    @NotBlank
    private String username;

    @Column(name = "email")
    @NotBlank
    @Email
    private String email;

    @Column(name = "password")
    @Length(min = 8, max = 255)
    @NotBlank
    private String password;

    @Column(name = "first_name")
    @Length(min = 3, max = 100)
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @Length(min = 3, max = 100)
    @NotBlank
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CompanyEmployee> jobs = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    private List<Category> categories;

    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<String> getRoles() {
        return jobs
                .stream()
                .map(emp -> emp.getRole().getName().name())
                .collect(Collectors.toList());
    }
}
