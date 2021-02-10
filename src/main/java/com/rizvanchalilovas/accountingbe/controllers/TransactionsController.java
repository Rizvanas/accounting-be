package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.responses.TransactionResponse;
import com.rizvanchalilovas.accountingbe.services.interfaces.TransactionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/companies/{companyId}/categories/{categoryId}/transactions")
public class TransactionsController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:read', 'admin:read', 'employee:read', 'guest:read')")
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllCategoryTransactions(
            @PathVariable Long companyId,
            @PathVariable Long categoryId
    ) throws NotFoundException {
        return ResponseEntity.ok(transactionService.findTransactionsByCategoryId(categoryId));
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:read', 'admin:read', 'employee:read', 'guest:read')")
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long companyId,
            @PathVariable Long categoryId,
            @PathVariable Long transactionId
    ) throws NotFoundException {
        return ResponseEntity.ok(transactionService.findTransactionById(transactionId));
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write') ||" +
            "(hasRequiredPermissions(#companyId, 'employee:write') && isResponsibleUser(#companyId, #categoryId))")
    @PostMapping
    public ResponseEntity<List<TransactionResponse>> addNewTransaction(
            @PathVariable Long companyId,
            @PathVariable Long categoryId,
            @RequestBody TransactionAdditionRequest request,
            UriComponentsBuilder builder
    ) throws NotFoundException {

        URI location = builder.replacePath("api/company/{companyId}/categories/{categoryId}/transactions")
                .buildAndExpand(companyId, categoryId).toUri();

        var response = transactionService.addTransactions(categoryId, request);

        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write') ||" +
            "(hasRequiredPermissions(#companyId, 'employee:write') && isResponsibleUser(#companyId, #categoryId))")
    @PatchMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long companyId,
            @PathVariable Long categoryId,
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequest request
    ) throws NotFoundException {

        var response = transactionService.updateTransaction(categoryId, transactionId, request);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRequiredPermissions(#companyId, 'ceo:write', 'admin:write') ||" +
            "(hasRequiredPermissions(#companyId, 'employee:write') && isResponsibleUser(#companyId, #categoryId))")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> removeTransaction(
            @PathVariable Long companyId,
            @PathVariable Long categoryId,
            @PathVariable Long transactionId
    ) throws NotFoundException {

        transactionService.removeTransaction(companyId, categoryId, transactionId);

        return ResponseEntity.noContent().build();
    }
}
