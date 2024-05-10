package com.booleanuk.requests.category;

import com.booleanuk.requests.expense.Expense;
import com.booleanuk.requests.incomes.Income;
import com.booleanuk.requests.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private ECategory type;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category")
    @JsonIncludeProperties(value = {"id"})
    private List<Expense> expenses;

    @OneToMany(mappedBy = "category")
    @JsonIncludeProperties(value = {"id"})
    private List<Income> incomes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties(value = {"id"})
    private User user;

    public Category(String name, ECategory type) {
        this.name = name;
        this.type = type;

    }

    public Category(int id) {
        this.id = id;
    }
}
