package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionAdditionRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.requests.TransactionUpdateRequest;
import com.rizvanchalilovas.accountingbe.dtos.transaction.responses.TransactionResponse;
import com.rizvanchalilovas.accountingbe.models.Category;
import com.rizvanchalilovas.accountingbe.models.Company;
import com.rizvanchalilovas.accountingbe.models.Transaction;
import com.rizvanchalilovas.accountingbe.models.TransactionType;
import com.rizvanchalilovas.accountingbe.repositories.CategoryJpaRepository;
import com.rizvanchalilovas.accountingbe.repositories.TransactionJpaRepository;
import com.rizvanchalilovas.accountingbe.services.interfaces.TransactionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionJpaRepository transactionsRepository;
    private final CategoryJpaRepository categoriesRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionJpaRepository transactionsRepository,
            CategoryJpaRepository categoriesRepository
    ) {
        this.transactionsRepository = transactionsRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<TransactionResponse> findTransactionsByCategoryId(Long id) throws NotFoundException {
        var transactions = transactionsRepository.findTransactionsByCategoryId(id);

        if (transactions.isEmpty()) {
            throw new NotFoundException("No transactions were found");
        }

        return transactions
                .stream().map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse findTransactionById(Long transactionId) throws NotFoundException {
        return transactionsRepository
                .findById(transactionId)
                .map(TransactionResponse::fromTransaction)
                .orElseThrow(() -> new NotFoundException("Transaction could not be found"));
    }

    @Override
    public List<TransactionResponse> addTransactions(
            Long categoryId,
            TransactionAdditionRequest... requests
    ) throws NotFoundException {

        var category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category could not be found"));

        return addTransactions(category, requests);
    }

    @Override
    public List<TransactionResponse> addTransactions(
            @NonNull Category category,
            TransactionAdditionRequest... requests
    ) {

        var transactions = Arrays.stream(requests).map(r -> new Transaction(
                r.getTitle(),
                r.getComment(),
                r.getMoneyAmount(),
                r.getType(),
                category
        )).collect(Collectors.toList());

        var responses = transactionsRepository.saveAll(transactions)
                .stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());

        updateIncomeAndExpenditure(category, transactions);
        categoriesRepository.saveAndFlush(category);

        return responses;
    }

    @Override
    public TransactionResponse updateTransaction(
            Long categoryId,
            Long transactionId,
            TransactionUpdateRequest request
    ) throws NotFoundException {
        var transaction = transactionsRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction with id: could not be found"));

        if (!transaction.getCategory().getId().equals(categoryId)) {
            throw new NotFoundException("Transaction with id: does not belong to a category with id:");
        }

        if (request.getTitle().isPresent() && !request.getTitle().get().isBlank()) {
            transaction.setTitle(request.getTitle().get());
        }

        if (request.getComment().isPresent() && !request.getComment().get().isBlank()) {
            transaction.setComment(request.getComment().get());
        }

        updateTransactionMoneyAndType(transaction, request);

        categoriesRepository.saveAndFlush(transaction.getCategory());

        return TransactionResponse.fromTransaction(transaction);
    }

    @Override
    public void removeTransaction(Long companyId, Long categoryId, Long transactionId) throws NotFoundException {
        var transaction = transactionsRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction with id: could not be found"));

        var category = transaction.getCategory();
        if (!transaction.getCategory().getId().equals(categoryId)) {
            throw new NotFoundException("Transaction with id: does not belong to a category with id:");
        }

        if (!category.getCompany().getId().equals(companyId)) {
            throw new NotFoundException("Category with id: does not belong to a company with id:");
        }

        var changeInIncome =  transaction.getType().equals(TransactionType.INCOME)
                ? -transaction.getMoneyAmount() : 0L;

        var changeInExpenditure = transaction.getType().equals(TransactionType.EXPENDITURE)
                ? - transaction.getMoneyAmount() : 0L;

        updateCompany(category.getCompany(), changeInIncome, changeInExpenditure);
        updateParentCategories(category, changeInIncome, changeInExpenditure);

        transactionsRepository.deleteById(transactionId);
    }

    private void updateIncomeAndExpenditure(Category category, List<Transaction> transactions) {

        if (transactions.isEmpty()) return;

        var changeInIncome = 0L;
        var changeInExpenditure = 0L;
        for (var transaction : transactions) {
            if (transaction.getType().equals(TransactionType.INCOME)) {
                changeInIncome += transaction.getMoneyAmount();
            } else if (transaction.getType().equals(TransactionType.EXPENDITURE)) {
                changeInExpenditure += transaction.getMoneyAmount();
            }
        }

        category.setTotalIncome(category.getTotalIncome() + changeInIncome);
        category.setTotalExpenditure(category.getTotalExpenditure() + changeInExpenditure);

        updateCompany(category.getCompany(), changeInIncome, changeInExpenditure);
        updateParentCategories(category, changeInIncome, changeInExpenditure);
    }

    private void updateTransactionMoneyAndType(Transaction transaction, TransactionUpdateRequest request) {

        if (!request.getMoneyAmount().isPresent() ||
                request.getMoneyAmount().get() == null ||
                request.getMoneyAmount().get() <= 0) {
            return;
        }

        var transactionType = transaction.getType();
        var requestType = request.getType().isPresent() && request.getType() != null ? request.getType().get() : transactionType;

        var oldIncome = 0L;
        var oldExpenditure = 0L;
        if (transactionType.equals(TransactionType.INCOME)) {
            oldIncome = transaction.getMoneyAmount();
        } else if (transactionType.equals(TransactionType.EXPENDITURE)) {
            oldExpenditure = transaction.getMoneyAmount();
        }

        var newIncome = 0L;
        var newExpenditure = 0L;
        if (requestType.equals(TransactionType.INCOME)) {
            newIncome = request.getMoneyAmount().get();
        } else if (requestType.equals(TransactionType.EXPENDITURE)) {
            newExpenditure = request.getMoneyAmount().get();
        }

        var changeInIncome = newIncome - oldIncome;
        var changeInExpenditure = newExpenditure - oldExpenditure;

        updateCompany(transaction.getCategory().getCompany(), changeInIncome, changeInExpenditure);
        updateParentCategories(transaction.getCategory(), changeInIncome, changeInExpenditure);
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
