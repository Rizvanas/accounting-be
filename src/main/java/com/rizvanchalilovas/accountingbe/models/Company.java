package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "companies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Company extends BaseEntity {
    @NotNull
    @Length(min = 3, max = 255)
    @Column(name = "name")
    private String name;

    @Length(max = 255)
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ceo_id")
    private User owner;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_income")
    private Long totalIncome = 0L;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_expenditure")
    private Long totalExpenditure = 0L;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CompanyEmployee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private List<Category> categories = new ArrayList<>();

    public Company(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public boolean employeeExists(User user) {
        return employees
                .stream()
                .anyMatch(emp -> emp.getUser().getId().equals(user.getId()));
    }
}
