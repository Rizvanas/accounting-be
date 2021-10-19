package com.rizvanchalilovas.accountingbe.services.interfaces;

import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryResponse;
import javassist.NotFoundException;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getCategoriesByCompanyId(Long companyId) throws NotFoundException;

    CategoryResponse findCategoryById(Long id) throws NotFoundException;

    CategoryResponse addNewCategory(Long companyId, CategoryAdditionRequest request) throws NotFoundException;

    CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request) throws NotFoundException;

    void removeCategory(Long companyId, Long categoryId) throws NotFoundException;
}
