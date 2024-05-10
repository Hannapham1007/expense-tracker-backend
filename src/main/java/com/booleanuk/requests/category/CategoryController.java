package com.booleanuk.requests.category;

import com.booleanuk.requests.responses.ApiResponse;
import com.booleanuk.requests.responses.Responses;
import com.booleanuk.requests.responses.ValidationUtils;
import com.booleanuk.requests.user.User;
import com.booleanuk.requests.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getAllCategories(){
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        List<Category> categories = categoryRepository.findAll();
        categoryListResponse.set(categories);
        return new ResponseEntity<>(categoryListResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CategoryListResponse> getCategories(@PathVariable int userId) {
        User user = ValidationUtils.getById(userId, userRepository);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Category> categories = categoryRepository.findByUser(user);
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        categoryListResponse.set(categories);
        return new ResponseEntity<>(categoryListResponse, HttpStatus.OK);
    }
    @GetMapping("/income/{userId}")
    public ResponseEntity<CategoryListResponse> getIncomeCategories(@PathVariable int userId){
        User user = ValidationUtils.getById(userId, userRepository);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CategoryListResponse incomeCategoryListResponse = new CategoryListResponse();
        List<Category> categories = categoryRepository.findByUserAndType(user, ECategory.CATEGORY_INCOME);
        incomeCategoryListResponse.set(categories);
        return new ResponseEntity<>(incomeCategoryListResponse, HttpStatus.OK);
    }

    @GetMapping("/expense/{userId}")
    public ResponseEntity<CategoryListResponse> getExpenseCategories(@PathVariable int userId){
        User user = ValidationUtils.getById(userId, userRepository);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CategoryListResponse expenseCategoryListResponse = new CategoryListResponse();
        List<Category> categories = categoryRepository.findByUserAndType(user,ECategory.CATEGORY_EXPENSE);
        expenseCategoryListResponse.set(categories);
        return new ResponseEntity<>(expenseCategoryListResponse, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody Category category){
        //int userId = category.getUser().getId();
       // User user = ValidationUtils.getById(userId, userRepository);
        if(ValidationUtils.isInvalidCategory(category)){
            return Responses.badRequest("create", category.getClass().getSimpleName());
        }
        Category createCategory = this.categoryRepository.save(category);
        //createCategory.setUser(user);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.set(createCategory);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategoryById(@PathVariable int id){
        Category categoryToDelete = ValidationUtils.getById(id, categoryRepository);
        if(categoryToDelete == null){
            return Responses.notFound("category");
        }
        this.categoryRepository.delete(categoryToDelete);
        CategoryResponse categoryResponse = new CategoryResponse();
        return ResponseEntity.ok(categoryResponse);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable int id, @RequestBody Category category){
        if(ValidationUtils.isInvalidCategory(category)){
            return Responses.badRequest("update", category.getClass().getSimpleName());
        }
        Category categoryToUpdate = ValidationUtils.getById(id, categoryRepository);
        if(categoryToUpdate == null){
            return Responses.notFound("category");
        }
        categoryToUpdate.setName(category.getName());
        this.categoryRepository.save(categoryToUpdate);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.set(categoryToUpdate);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }


    /*@PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody Category category){
        if(ValidationUtils.isInvalidCategory(category)){
            return Responses.badRequest("create", category.getClass().getSimpleName());
        }
        Category createdCategory = this.categoryRepository.save(category);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.set(createdCategory);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
*/


  /*  @GetMapping("/income")
    public ResponseEntity<CategoryListResponse> getIncomeCategories(){
        CategoryListResponse incomeCategoryListResponse = new CategoryListResponse();
        List<Category> categories = categoryRepository.findByType(ECategory.CATEGORY_INCOME);
        incomeCategoryListResponse.set(categories);
        return new ResponseEntity<>(incomeCategoryListResponse, HttpStatus.OK);
    }*/
   /* @GetMapping("/expense")
    public ResponseEntity<CategoryListResponse> getExpenseCategories(){
        CategoryListResponse expenseCategoryListResponse = new CategoryListResponse();
        List<Category> categories = categoryRepository.findByType(ECategory.CATEGORY_EXPENSE);
        expenseCategoryListResponse.set(categories);
        return new ResponseEntity<>(expenseCategoryListResponse, HttpStatus.OK);
    }*/

}
