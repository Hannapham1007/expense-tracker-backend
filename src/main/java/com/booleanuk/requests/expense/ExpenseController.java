package com.booleanuk.requests.expense;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.category.CategoryRepository;
import com.booleanuk.requests.responses.ApiResponse;
import com.booleanuk.requests.responses.Responses;
import com.booleanuk.requests.responses.ValidationUtils;
import com.booleanuk.requests.user.User;
import com.booleanuk.requests.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ExpenseListResponse> getAllExpense(){
        ExpenseListResponse expenseListResponse = new ExpenseListResponse();
        List<Expense> expenses = this.expenseRepository.findAll();
        expenseListResponse.set(expenses);
        return new ResponseEntity<>(expenseListResponse, HttpStatus.OK);
    }
  @PostMapping
  public ResponseEntity<ApiResponse<?>> createExpense(@RequestBody Expense expense){
      int categoryId = expense.getCategory().getId();
      int userId = expense.getUser().getId();

      Category category = ValidationUtils.getById(categoryId, categoryRepository);
      User user = ValidationUtils.getById(userId, userRepository);

      if(category == null || user == null){
          return Responses.badRequest("create", "expense");
      }

      if(ValidationUtils.isInvalidExpense(expense)){
          return Responses.badRequest("create", "expense");
      }
      Expense createExpense = this.expenseRepository.save(expense);

      createExpense.setCategory(category);
      createExpense.setUser(user);
      ExpenseResponse expenseResponse = new ExpenseResponse();
      expenseResponse.set(createExpense);
      return new ResponseEntity<>(expenseResponse, HttpStatus.CREATED);
  }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteExpense(@PathVariable int id){
        Expense expenseToDelete = ValidationUtils.getById(id, expenseRepository);
        if(expenseToDelete == null){
            return Responses.notFound("expense");
        }
        this.expenseRepository.delete(expenseToDelete);
        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.set(expenseToDelete);
        return ResponseEntity.ok(expenseResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateExpense(@PathVariable int id, @RequestBody Expense expense){
        int categoryId = expense.getCategory().getId();
        int userId = expense.getUser().getId();
        Category category = ValidationUtils.getById(categoryId, categoryRepository);
        User user = ValidationUtils.getById(userId, userRepository);

        Expense expenseToUpdate = ValidationUtils.getById(id,expenseRepository);

        if(ValidationUtils.isInvalidExpense(expense)){
            return Responses.badRequest("update", expense.getClass().getSimpleName());
        }
        if(expenseToUpdate == null){
            return Responses.notFound("expense");
        }
        if(category == null ||user == null){
            return Responses.badRequest("create", "expense");
        }

        expenseToUpdate.setAmount(expense.getAmount());
        expenseToUpdate.setCategory(expense.getCategory());
        expenseToUpdate.setDescription(expense.getDescription());
        expenseToUpdate.setExpenseDate(expense.getExpenseDate());
        this.expenseRepository.save(expenseToUpdate);
        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.set(expenseToUpdate);
        return new ResponseEntity<>(expenseResponse,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getAllExpenseByUserId(@PathVariable int id) {
        ExpenseListResponse expenseListResponse = new ExpenseListResponse();
        User user = ValidationUtils.getById(id, userRepository);

        if (user == null) {
            return Responses.notFound("User not found");
        }

        List<Expense> expenses = this.expenseRepository.findByUser(user);

        expenseListResponse.set(expenses);
        return new ResponseEntity<>(expenseListResponse, HttpStatus.OK);
    }

}
