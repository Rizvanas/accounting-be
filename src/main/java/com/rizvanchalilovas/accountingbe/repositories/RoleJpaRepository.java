package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.Role;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
