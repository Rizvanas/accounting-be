package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction extends BaseEntity {
    @Column(name = "title")
    @NotBlank(message = "title is required")
    @Length(min = 3, max = 255)
    private String title;

    @Column(name = "comment")
    @Length(min = 3, max = 255, message = "field comment must be between 3 and 255 characters long.")
    private String comment;

    @Column(name = "money_amount")
    @NotNull(message = "moneyAmount cannot be null")
    @Positive(message = "moneyAmount value must be greater than 0.")
    private Long moneyAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction(
            @NotBlank(message = "title is required") @Length(min = 3, max = 255) String title,
            @Length(min = 3, max = 255, message = "field comment must be between 3 and 255 characters long.") String comment,
            @NotNull(message = "moneyAmount cannot be null")
            @Positive(message = "moneyAmount value must be greater than 0.") Long moneyAmount,
            TransactionType type,
            Category category
    ) {
        this.title = title;
        this.comment = comment;
        this.moneyAmount = moneyAmount;
        this.type = type;
        this.category = category;
    }
}
