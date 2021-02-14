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
    @NotBlank
    @Length(min = 3, max = 255)
    @Column(name = "title")
    private String title;

    @Length(min = 3, max = 255)
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Positive
    @Column(name = "money_amount")
    private Long moneyAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction(
            @NotBlank @Length(min = 3, max = 255) String title,
            @NotBlank @Length(min = 3, max = 255) String comment,
            @NotNull @Positive Long moneyAmount,
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
