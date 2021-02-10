package com.rizvanchalilovas.accountingbe.services.interfaces;

import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.responses.TransactionResponse;
import javassist.NotFoundException;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> findTransactionsByCategoryId(Long categoryId) throws NotFoundException;

    TransactionResponse findTransactionById(Long transactionId) throws NotFoundException;

    List<TransactionResponse> addTransactions(
            Long categoryId,
            TransactionAdditionRequest... requests) throws NotFoundException;

    TransactionResponse updateTransaction(
            Long categoryId,
            Long transactionId,
            TransactionUpdateRequest request
    ) throws NotFoundException;

    void removeTransaction(
            Long companyId,
            Long categoryId,
            Long transactionId
    ) throws NotFoundException, IllegalStateException;
}
