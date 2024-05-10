package com.booleanuk.requests.incomes;

import com.booleanuk.requests.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findByUser(User user);
}
