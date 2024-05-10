package com.booleanuk.requests.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responses {
    public static ResponseEntity<ApiResponse<?>> badRequest(String operation, String object){
        ErrorResponse error = new ErrorResponse();
        error.set("Could not " + operation +  " " + object +", please check all required fields are correct");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ApiResponse<?>> notFound(String object){
        ErrorResponse error = new ErrorResponse();
        error.set("No " + object + " with that id were found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
