package com.rizvanchalilovas.accountingbe.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;

        Permission other = (Permission) o;

        return Objects.equals(this.name, other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
