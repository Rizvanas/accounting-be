package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.requests.CompanyUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.company.responses.CompanyDetailsResponse;
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
    public ResponseEntity<List<Company>> list(Principal principal)
            throws NotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyService.getAllCompanies());
    }

    @PostMapping
    public ResponseEntity<CompanyDetailsResponse> add(@Valid @RequestBody CompanyAdditionRequest request)
            throws NotFoundException, AlreadyExistsException {
        var companyResponse = companyService.addNewCompany(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyResponse);
    }

    @PreAuthorize("(#request.ownerUsername == null" +
            " and hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write')) " +
            "or hasRequiredPermissions(#companyId, 'ceo:write')")
    @PatchMapping("/{companyId}")
    public ResponseEntity<CompanyDetailsResponse> update(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyUpdateRequest request)
            throws NotFoundException {

        var companyResponse = companyService.updateCompanyDetails(companyId, request);
        return ResponseEntity.ok().build();
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
