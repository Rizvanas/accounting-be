package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.CompanyEmployee;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyEmployeeJpaRepository extends JpaRepository<CompanyEmployee, Long> {
    Optional<CompanyEmployee> findByUserId(Long userId);
    Optional<CompanyEmployee> findByUserUsername(String username);
    Optional<CompanyEmployee> findFirstByRoleName(RoleEnum roleName);
}
