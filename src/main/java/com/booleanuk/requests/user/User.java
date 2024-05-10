package com.booleanuk.requests.user;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.expense.Expense;
import com.booleanuk.requests.incomes.Income;
import com.booleanuk.requests.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @Column
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<Income> incomes;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<Category> categories;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    public User( String username,  String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(int id) {
        this.id = id;
    }
}
