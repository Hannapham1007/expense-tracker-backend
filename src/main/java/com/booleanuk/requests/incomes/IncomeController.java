package com.booleanuk.requests.incomes;

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

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<IncomeListResponse> getAllIncome(){
        IncomeListResponse incomeListResponse = new IncomeListResponse();
        List<Income> incomes = this.incomeRepository.findAll();
        incomeListResponse.set(incomes);
        return new ResponseEntity<>(incomeListResponse, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createIncome (@RequestBody Income income){
        int categoryId = income.getCategory().getId();
        int userId = income.getUser().getId();

        Category category = ValidationUtils.getById(categoryId, categoryRepository);
        User user = ValidationUtils.getById(userId, userRepository);

        if(ValidationUtils.isInvalidIncome(income)){
            return Responses.badRequest("create", "income");
        }

        if(category == null || user == null){
            return Responses.badRequest("create", "income");
        }

        Income createIncome = this.incomeRepository.save(income);
        createIncome.setUser(user);
        createIncome.setCategory(category);
        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.set(createIncome);
        return new ResponseEntity<>(incomeResponse, HttpStatus.CREATED);
    }
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteIncome(@PathVariable int id){
        Income incomeToDelete = ValidationUtils.getById(id, incomeRepository);
        if(incomeToDelete == null){
            return Responses.notFound("income");
        }
        this.incomeRepository.delete(incomeToDelete);
        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.set(incomeToDelete);
        return ResponseEntity.ok(incomeResponse);
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateIncome(@PathVariable int id, @RequestBody Income income){
        int categoryId = income.getCategory().getId();
        int userId = income.getUser().getId();
        Category category = ValidationUtils.getById(categoryId, categoryRepository);
        User user = ValidationUtils.getById(userId, userRepository);

        Income incomeToUpdate = ValidationUtils.getById(id,incomeRepository);

        if(ValidationUtils.isInvalidIncome(income)){
            return Responses.badRequest("update", income.getClass().getSimpleName());
        }
        if(incomeToUpdate == null){
            return Responses.notFound("income");
        }
        if(category == null ||user == null){
            return Responses.badRequest("create", "income");
        }

        incomeToUpdate.setAmount(income.getAmount());
        incomeToUpdate.setCategory(income.getCategory());
        incomeToUpdate.setDescription(income.getDescription());
        incomeToUpdate.setIncomeDate(income.getIncomeDate());
        this.incomeRepository.save(incomeToUpdate);
        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.set(incomeToUpdate);
        return new ResponseEntity<>(incomeResponse,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getAllIncomeByUserId(@PathVariable int id) {
        IncomeListResponse incomeListResponse = new IncomeListResponse();
        User user = ValidationUtils.getById(id, userRepository);

        if (user == null) {
            return Responses.notFound("User not found");
        }

        List<Income> incomes = this.incomeRepository.findByUser(user);

        incomeListResponse.set(incomes);
        return new ResponseEntity<>(incomeListResponse, HttpStatus.OK);
    }
}
