package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeInvitationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.EmployeeRemovalRequest;
import com.rizvanchalilovas.accountingbe.services.interfaces.CompanyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/companies/{companyId}/employees")
public class EmployeesController {
    private final CompanyService companyService;

    @Autowired
    public EmployeesController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write')")
    @PostMapping
    public ResponseEntity<?> inviteEmployee(
            @PathVariable Long companyId,
            @Valid @RequestBody EmployeeInvitationRequest request
    ) throws NotFoundException {

        var employeesResponse = companyService.addEmployee(companyId, request);

        return ResponseEntity.ok(employeesResponse);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write')")
    @DeleteMapping
    public ResponseEntity<?> removeEmployee(
            @PathVariable Long companyId,
            @Valid @RequestBody EmployeeRemovalRequest request
    ) throws NotFoundException {

        companyService.removeEmployee(companyId, request);

        return ResponseEntity.ok("Employee was successfully removed");
    }
}
