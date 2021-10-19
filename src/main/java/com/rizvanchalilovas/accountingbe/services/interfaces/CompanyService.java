package com.rizvanchalilovas.accountingbe.services.interfaces;

import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyListItem;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeInvitationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeRemovalRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.EmployeeResponse;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.Company;
import javassist.NotFoundException;

import java.util.List;

public interface CompanyService {
    List<CompanyListItem> getAllCompanies() throws NotFoundException;
    List<EmployeeResponse> addEmployee(Long companyId, EmployeeInvitationRequest request) throws NotFoundException, AlreadyExistsException;
    CompanyListItem addNewCompany(CompanyAdditionRequest request) throws NotFoundException, AlreadyExistsException;
    void removeEmployee(Long companyId, EmployeeRemovalRequest request) throws NotFoundException;
    void removeCompany(Long companyId) throws NotFoundException;

    CompanyDetailsResponse updateCompanyDetails(Long companyId, CompanyUpdateRequest request) throws NotFoundException;

    CompanyDetailsResponse getById(Long companyId) throws NotFoundException;
}
