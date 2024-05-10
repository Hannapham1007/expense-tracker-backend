package com.booleanuk.requests.incomes;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="incomes")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double amount;

    @Column
    private String description;

    @Column
    private LocalDateTime incomeDate;

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

    public Income(double amount, String description, LocalDateTime incomeDate) {
        this.amount = amount;
        this.description = description;
        this.incomeDate = incomeDate;
    }

    public Income(int id) {
        this.id = id;
    }
}
