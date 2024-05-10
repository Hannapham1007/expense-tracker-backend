package com.booleanuk.requests.expense;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double amount;

    @Column
    private String description;

    @Column
    private LocalDateTime expenseDate;

    @Column
    @UpdateTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id", "name"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIncludeProperties({"id", "name"})
    private Category category;


    public Expense(double amount, String description, LocalDateTime expenseDate) {
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
    }
    public Expense(int id) {
        this.id = id;
    }
}
