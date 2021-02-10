package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeInvitationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeRemovalRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.models.CompanyEmployee;
import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import com.rizvanchalilovas.accountingbe.repositories.CompanyEmployeeJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.CompanyJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.RoleJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.UserJpaRepository;
import com.rizvanchalilovas.accountingbe.services.interfaces.CompanyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyJpaRepository companyRepository;
    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final CompanyEmployeeJpaRepository employeeRepository;

    @Autowired
    public CompanyServiceImpl(
            CompanyJpaRepository companyRepository,
            UserJpaRepository userRepository,
            RoleJpaRepository roleRepository,
            CompanyEmployeeJpaRepository employeeRepository
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Company> getAllCompanies() throws NotFoundException {
        var companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            throw new NotFoundException("No companies were found.");
        }

        return companies;
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

        employeeRepository.delete(employee);
        company.getEmployees().remove(employee);
        companyRepository.saveAndFlush(company);
    }

    @Override
    public CompanyDetailsResponse addNewCompany(CompanyAdditionRequest request)
            throws NotFoundException, AlreadyExistsException {

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User could not be found"));

        if (companyRepository.existsByName(request.getCompanyName())) {
            throw new AlreadyExistsException("Company with name: already exists");
        }

        var company = new Company(request.getCompanyName(), request.getDescription(), user);
        var role = roleRepository.findByName(RoleEnum.CEO);
        var companyEmployee = new CompanyEmployee(company, user, role);

        role.getEmployees().add(companyEmployee);
        company = companyRepository.saveAndFlush(company);

        return CompanyDetailsResponse.fromCompany(company);
    }

    @Override
    public CompanyDetailsResponse updateCompanyDetails(Long companyId, CompanyUpdateRequest request)
            throws NotFoundException {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not fe found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            company.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            company.setDescription(request.getDescription());
        }

        if (request.getOwnerUsername() != null && !request.getOwnerUsername().isBlank()) {
            var employee = employeeRepository.findByUserUsername(request.getOwnerUsername())
                    .orElseThrow(() -> new NotFoundException("User could not be found"));

            var currentCeo = employeeRepository
                    .findByUserUsername(company.getOwner().getUsername())
                    .orElseThrow(() -> new NotFoundException("User could not be found"));

            var ceoRole = roleRepository.findByName(RoleEnum.CEO);
            var adminRole = roleRepository.findByName(RoleEnum.ADMIN);

            employee.setRole(ceoRole);
            currentCeo.setRole(adminRole);
            company.setOwner(employee.getUser());
        }

        companyRepository.saveAndFlush(company);

        return CompanyDetailsResponse.fromCompany(company);
    }

    @Override
    public void removeCompany(Long companyId) throws NotFoundException {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id: could not fe found"));

        companyRepository.delete(company);
    }
}
