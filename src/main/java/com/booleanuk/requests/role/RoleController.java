package com.booleanuk.requests.role;
import com.booleanuk.requests.responses.ApiResponse;
import com.booleanuk.requests.responses.Responses;
import com.booleanuk.requests.responses.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<RoleListResponse> getAllRoles(){
        RoleListResponse roleListResponse = new RoleListResponse();
        List<Role> roles = this.roleRepository.findAll();
        roleListResponse.set(roles);
        return new ResponseEntity<>(roleListResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRole(@Valid @RequestBody Role role){
        if(ValidationUtils.isInvalidRole(role)){
            return Responses.badRequest("create", role.getClass().getSimpleName());
        }
        Role createdRole = this.roleRepository.save(role);
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.set(createdRole);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }


}