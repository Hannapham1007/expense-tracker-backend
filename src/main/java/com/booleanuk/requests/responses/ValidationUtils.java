package com.booleanuk.requests.responses;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.expense.Expense;
import com.booleanuk.requests.incomes.Income;
import com.booleanuk.requests.role.Role;
import com.booleanuk.requests.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public class ValidationUtils {
    public static boolean isInvalidUser(User user){
        return user.getUsername() == null || user.getPassword() == null || user.getEmail() == null;
    }

    public static  boolean isInvalidExpense(Expense expense){
        return expense.getAmount() < 0 || expense.getDescription()== null || expense.getUser() == null;
    }
    public static  boolean isInvalidIncome(Income income){
        return income.getAmount() < 0 || income.getDescription()== null || income.getUser() == null || income.getCategory() == null;
    }

    public static boolean isInvalidCategory(Category category){
        return category.getName() == null || category.getType() == null;
    }
    public static boolean isInvalidRole(Role role){
        return role.getName() == null;
    }
    public static <T> T getById(int id, JpaRepository<T, Integer> repository) {
        return repository.findById(id).orElse(null);
    }
}
