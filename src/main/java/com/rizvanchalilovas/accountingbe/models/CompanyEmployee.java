package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name = "company_employees")
public class CompanyEmployee extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    public CompanyEmployee(Company company, User user, Role role) {
        this.company = company;
        this.user = user;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyEmployee)) return false;

        CompanyEmployee other = (CompanyEmployee) o;

        return Objects.equals(company.getName(), other.getCompany().getName()) &&
                Objects.equals(user.getFullName(), other.getUser().getFullName()) &&
                Objects.equals(role.getName(), other.getRole().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(company.getName(), user.getFullName(), role.getName());
    }
}
