package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyDetailsResponse;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyListItem;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.services.interfaces.CompanyService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompaniesController {

    private final CompanyService companyService;

    public CompaniesController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<?> list(Principal principal)
            throws NotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyService.getAllCompanies());
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:read', 'admin:read', 'employee:read', 'guest:read')")
    @GetMapping("/{companyId}")
    public ResponseEntity<?> get(@PathVariable Long companyId) throws NotFoundException {
        var response = companyService.getById(companyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody CompanyAdditionRequest request)
            throws NotFoundException, AlreadyExistsException {
        var companyResponse = companyService.addNewCompany(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyResponse);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write')")
    @PutMapping("/{companyId}")
    public ResponseEntity<?> update(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyUpdateRequest request)
            throws NotFoundException {

        var companyResponse = companyService.updateCompanyDetails(companyId, request);
        return ResponseEntity.ok()
                .body(companyResponse);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write')")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> removeCompany(@PathVariable Long companyId)
            throws NotFoundException {

        companyService.removeCompany(companyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Company was successfully removed");
    }
}
