package com.booleanuk.requests.user;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.category.CategoryRepository;
import com.booleanuk.requests.category.ECategory;
import com.booleanuk.requests.responses.ApiResponse;
import com.booleanuk.requests.responses.Responses;
import com.booleanuk.requests.responses.ValidationUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(){
        UserListResponse userListResponse = new UserListResponse();
        List<User> users = this.userRepository.findAll();
        userListResponse.set(users);
        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable int id){
        User user = ValidationUtils.getById(id, userRepository);
        if(user == null){
            return Responses.notFound("user");
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable int id, @RequestBody User user){
        if(ValidationUtils.isInvalidUser(user)){
            return Responses.badRequest("update", user.getClass().getSimpleName());
        }
        User userToUpdate = ValidationUtils.getById(id, userRepository);
        if(userToUpdate == null){
            return Responses.notFound("user");
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        this.userRepository.save(userToUpdate);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToUpdate);
        return new ResponseEntity<>(userResponse,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable int id){
        User userToDelete = ValidationUtils.getById(id, userRepository);
        if(userToDelete == null){
            return Responses.notFound("user");
        }
        this.userRepository.delete(userToDelete);
        UserResponse userResponse = new UserResponse();
        return ResponseEntity.ok(userResponse);
    }

}
