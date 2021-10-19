package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.requests.CategoryUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.category.responses.CategoryResponse;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import com.rizvanchalilovas.accountingbe.models.Category;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.repositories.CategoryJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.CompanyEmployeeJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.CompanyJpaRepository;
import com.rizvanchalilovas.accountingbe.services.interfaces.CategoryService;
import com.rizvanchalilovas.accountingbe.services.interfaces.TransactionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryRepository;
    private final CompanyJpaRepository companyRepository;
    private final CompanyEmployeeJpaRepository employeeRepository;
    private final TransactionService transactionService;

    @Autowired
    public CategoryServiceImpl(
            CategoryJpaRepository categoryRepository,
            CompanyJpaRepository companyRepository,
            CompanyEmployeeJpaRepository employeeRepository,
            TransactionService transactionService
    ) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.transactionService = transactionService;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse addNewCategory(
            Long companyId,
            CategoryAdditionRequest request
    ) throws NotFoundException {

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not be found"));

        var employee = employeeRepository.findById(request.getManagingEmployeeId())
                .orElseThrow(() -> new NotFoundException("Responsible user could not be found"));

        var parent = request.getParentId() == null ? null :
                categoryRepository.findById(request.getParentId()).orElse(null);

        var category = new Category(request.getTitle(), request.getDescription().get(), employee, parent, company);
        category = categoryRepository.save(category);

        transactionService.addTransactions(
                category,
                request.getTransactions().toArray(new TransactionAdditionRequest[0])
        );

        categoryRepository.saveAndFlush(category);

        return categoryRepository.findById(category.getId())
                .map(CategoryResponse::fromCategory)
                .orElseThrow(() -> new NotFoundException("Category with id: could not found"));
    }

    @Override
    public List<CategoryResponse> getCategoriesByCompanyId(Long companyId) throws NotFoundException {

        var categories = categoryRepository.findAllByCompanyIdAndParentNull(companyId);

        if (categories.isEmpty()) {
            throw new NotFoundException("No categories were found");
        }

        return categories.stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findCategoryById(Long id) throws NotFoundException {
        return categoryRepository.findById(id)
                .map(CategoryResponse::fromCategory)
                .orElseThrow(() -> new NotFoundException("Category with id: could not be found"));
    }

    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request)
            throws NotFoundException {

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id: could not be found"));

        if (request.getTitle().isPresent()) {
            category.setTitle(request.getTitle().get());
        }

        if (request.getDescription().isPresent()) {
            category.setDescription(request.getDescription().get());
        }

        if (request.getManagingEmployeeId().isPresent()) {
            var employee = employeeRepository
                    .findById(request.getManagingEmployeeId().get())
                    .orElseThrow(() -> new NotFoundException("Employee with id: could not be found"));

            category.setManager(employee);
        }

        if (request.getParentId().isPresent()) {
            var newParent = request.getParentId().get() == null ?
                    null : categoryRepository.findById(request.getParentId().get())
                    .orElseThrow(() -> new NotFoundException("Category with id: could not be found"));

            updateParentCategories(category, -category.getTotalIncome(), -category.getTotalExpenditure());
            category.setParent(newParent);
            updateParentCategories(category, category.getTotalIncome(), category.getTotalExpenditure());
        }

        category = categoryRepository.saveAndFlush(category);

        return CategoryResponse.fromCategory(category);
    }

    @Override
    public void removeCategory(Long companyId, Long categoryId) throws NotFoundException, IllegalStateException {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id: could not be found"));

        if (!category.getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("Category with id: does not belong to a company with id:");
        }

        var changeInIncome = -category.getTotalIncome();
        var changeInExpenditure = -category.getTotalExpenditure();

        updateCompany(category.getCompany(), changeInIncome, changeInExpenditure);
        updateParentCategories(category, changeInIncome, changeInExpenditure);

        categoryRepository.deleteById(categoryId);
    }

    private void updateCompany(Company company, Long changeInIncome, Long changeInExpenditure) {
        var totalIncome = company.getTotalIncome() + changeInIncome;
        var totalExpenditure = company.getTotalExpenditure() + changeInExpenditure;

        company.setTotalIncome(totalIncome);
        company.setTotalExpenditure(totalExpenditure);
    }

    private void updateParentCategories(Category category, Long changeInIncome, Long changeInExpenditure) {
        var parent = category.getParent();
        while (parent != null) {
            parent.setTotalIncome(parent.getTotalIncome() + changeInIncome);
            parent.setTotalExpenditure(parent.getTotalExpenditure() + changeInExpenditure);
            parent = parent.getParent();
        }
    }
}


