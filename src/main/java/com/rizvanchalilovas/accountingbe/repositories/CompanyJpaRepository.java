package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
    Company findByName(String name);
    Boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO company_employees (company_id, employee_id, role_id) " +
            "values (:companyId, :userId, :roleId)", nativeQuery = true)
    int addNewEmployee(
            @Param("companyId") Long companyId,
            @Param("userId") Long userId,
            @Param("roleId") Long roleId
    );

    @Query("SELECT c FROM Company c LEFT JOIN c.employees employees WHERE employees.user.email LIKE %?1%")
    List<Company> findAllByEmployeesUserEmailContains(String email);
}
