package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryResponse;
import com.rizvanchalilovas.accountingbe.services.interfaces.CategoryService;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/companies/{companyId}/categories")
public class CategoriesController {
    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:read', 'admin:read', 'employee:read', 'guest:read')")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@PathVariable Long companyId) throws NotFoundException {
        var categories = categoryService.getCategoriesByCompanyId(companyId);

        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:read', 'admin:read', 'employee:read', 'guest:read')")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailsResponse> get(
            @PathVariable Long companyId,
            @PathVariable Long categoryId
    ) throws NotFoundException {

        var category = categoryService.findCategoryById(categoryId);

        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write', 'employee:write')")
    @PostMapping
    public ResponseEntity<CategoryResponse> addNewCategory(
            @PathVariable Long companyId,
            @Valid @RequestBody CategoryAdditionRequest request,
            UriComponentsBuilder builder
    ) throws NotFoundException {
        var response = categoryService.addNewCategory(companyId, request);

        URI location = builder.replacePath("api/company/{companyId}/categories/{categoryId}")
                .buildAndExpand(companyId, response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write') ||" +
            "(hasRequiredPermissions(#companyId, 'employee:write') && isResponsibleUser(#companyId, #categoryId))")
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long companyId,
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request,
            UriComponentsBuilder builder
    ) throws NotFoundException {
        var response = categoryService.updateCategory(categoryId, request);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write') ||" +
            "(hasRequiredPermissions(#companyId, 'employee:write') && isResponsibleUser(#companyId, #categoryId))")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> removeCategory(
            @PathVariable Long companyId,
            @PathVariable Long categoryId
    ) throws NotFoundException {

        categoryService.removeCategory(companyId, categoryId);

        return ResponseEntity.noContent().build();
    }
}
