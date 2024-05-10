package com.booleanuk.requests.security.jwt;

import com.booleanuk.requests.category.Category;
import com.booleanuk.requests.category.CategoryRepository;
import com.booleanuk.requests.category.ECategory;
import com.booleanuk.requests.payload.request.LoginRequest;
import com.booleanuk.requests.payload.request.SignupRequest;
import com.booleanuk.requests.payload.response.JwtResponse;
import com.booleanuk.requests.payload.response.MessageResponse;
import com.booleanuk.requests.role.ERole;
import com.booleanuk.requests.role.Role;
import com.booleanuk.requests.role.RoleRepository;
import com.booleanuk.requests.security.services.UserDetailsImpl;
import com.booleanuk.requests.user.User;
import com.booleanuk.requests.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // If using a salt for password use it here
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map((item) -> item.getAuthority())
                .collect(Collectors.toList());

        initDefaultCategories();

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }


        // Create a new user add salt here if using one
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach((role) -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok((new MessageResponse("User registered successfully")));
    }
    public void initDefaultCategories() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Category> existingCategories = categoryRepository.findByUser(user);
            if (existingCategories.isEmpty()) {
                addDefaultCategories(user);
            }
        }
    }

    private void addDefaultCategories(User user) {
        Category category1 = new Category();
        category1.setName("\uD83D\uDCB0 Salary");
        category1.setType(ECategory.CATEGORY_INCOME);
        category1.setUser(user);

        Category category2 = new Category();
        category2.setName("\uD83E\uDD47 Bonus");
        category2.setType(ECategory.CATEGORY_INCOME);
        category2.setUser(user);

        Category category3 = new Category();
        category3.setName("\uD83D\uDE97 Transport");
        category3.setType(ECategory.CATEGORY_EXPENSE);
        category3.setUser(user);

        Category category4 = new Category();
        category4.setName("\uD83C\uDFE0 Household");
        category4.setType(ECategory.CATEGORY_EXPENSE);
        category4.setUser(user);

        Category category5 = new Category();
        category5.setName("\uD83C\uDFCB\uFE0F Health");
        category5.setType(ECategory.CATEGORY_EXPENSE);
        category5.setUser(user);

        Category category6 = new Category();
        category6.setName("\uD83E\uDD57 Food");
        category6.setType(ECategory.CATEGORY_EXPENSE);
        category6.setUser(user);

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
        categoryRepository.save(category6);
    }

}