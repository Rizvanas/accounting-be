package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category extends BaseEntity {
    @NotBlank
    @Length(min = 3, max = 50)
    @Column(name = "title")
    private String title;

    @Length(max = 255)
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private CompanyEmployee manager;

    @Column(name = "total_income")
    @PositiveOrZero
    @NotNull
    private Long totalIncome = 0L;

    @Column(name = "total_expenditure")
    @PositiveOrZero
    @NotNull
    private Long totalExpenditure = 0L;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private List<Category> subCategories = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Category(
            String title,
            String description,
            CompanyEmployee manager,
            Category parent,
            Company company
    ) {
        this.title = title;
        this.description = description;
        this.manager = manager;
        this.parent = parent;
        this.company = company;
    }
}
