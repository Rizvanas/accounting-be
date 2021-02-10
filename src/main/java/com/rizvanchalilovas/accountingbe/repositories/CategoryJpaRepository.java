package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByCompanyId(Long companyId);
}
