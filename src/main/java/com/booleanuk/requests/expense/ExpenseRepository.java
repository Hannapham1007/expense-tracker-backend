package com.booleanuk.requests.expense;

import com.booleanuk.requests.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByUser(User user);
}
