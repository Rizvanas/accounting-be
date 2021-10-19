package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyListItem;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeInvitationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeRemovalRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.models.CompanyEmployee;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import com.rizvanchalilovas.accountingbe.models.User;
import com.rizvanchalilovas.accountingbe.repositories.*;
import com.rizvanchalilovas.accountingbe.services.interfaces.CompanyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyJpaRepository companyRepository;
    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final CompanyEmployeeJpaRepository employeeRepository;
    private final CategoryJpaRepository categoryRepository;

    @Autowired
    public CompanyServiceImpl(
            CompanyJpaRepository companyRepository,
            UserJpaRepository userRepository,
            RoleJpaRepository roleRepository,
            CompanyEmployeeJpaRepository employeeRepository,
            CategoryJpaRepository categoryRepository
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CompanyListItem> getAllCompanies() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        var companies = companyRepository.findAllByEmployeesUserEmailContains(loggedInEmail);

        if (companies.isEmpty()) {
            throw new NotFoundException("No companies were found.");
        }

        return companies.stream()
                .map(company -> CompanyListItem.fromCompany(company, loggedInEmail))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDetailsResponse getById(Long companyId) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        var categories = categoryRepository.findAllByCompanyIdAndParentNull(companyId);
        company.setCategories(categories);

        return CompanyDetailsResponse.fromCompany(company, loggedInEmail);
    }

    @Override
    public List<EmployeeResponse> addEmployee(Long companyId, EmployeeInvitationRequest request)
            throws NotFoundException, AlreadyExistsException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if (company.employeeExists(user)) {
            throw new AlreadyExistsException("User is already an employee of this company");
        }
        var role = roleRepository.findByName(RoleEnum.EMPLOYEE);

        var companyEmployee = new CompanyEmployee(company, user, role);
        role.getEmployees().add(companyEmployee);

        company = companyRepository.saveAndFlush(company);

        return company
                .getEmployees()
                .stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public void removeEmployee(Long companyId, EmployeeRemovalRequest request) throws NotFoundException {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not fe found"));

        var employee = employeeRepository.findByUserUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User is not an employee of this company"));

        //employeeRepository.delete(employee);
        System.out.println(employee.getUser().getUsername());
        employeeRepository.deleteById(employee.getId());
        employeeRepository.flush();
//        company.getEmployees().remove(employee);
//        companyRepository.saveAndFlush(company);
    }

    @Override
    public CompanyListItem addNewCompany(CompanyAdditionRequest request)
            throws NotFoundException, AlreadyExistsException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        var user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new NotFoundException("User could not be found"));

        if (companyRepository.existsByName(request.getCompanyName())) {
            throw new AlreadyExistsException("Company with name: already exists");
        }

        var company = new Company(request.getCompanyName(), request.getDescription().get());
        var role = roleRepository.findByName(RoleEnum.CEO);
        var companyEmployee = new CompanyEmployee(company, user, role);

        role.getEmployees().add(companyEmployee);
        company.getEmployees().add(companyEmployee);
        company = companyRepository.saveAndFlush(company);

        return CompanyListItem.fromCompany(company, loggedInEmail);
    }

    @Override
    public CompanyDetailsResponse updateCompanyDetails(Long companyId, CompanyUpdateRequest request)
            throws NotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not fe found"));

        if (request.getName().isPresent()) {
            company.setName(request.getName().get());
        }

        if (request.getDescription().isPresent()) {
            company.setDescription(request.getDescription().get());
        }

        companyRepository.saveAndFlush(company);

        return CompanyDetailsResponse.fromCompany(company, loggedInEmail);
    }

    @Override
    public void removeCompany(Long companyId) throws NotFoundException {

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not fe found"));

        companyRepository.delete(company);
    }
}
