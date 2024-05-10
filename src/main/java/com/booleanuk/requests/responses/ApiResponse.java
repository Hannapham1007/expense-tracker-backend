package com.booleanuk.requests.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {
    protected String status;
    protected T data;

    public void set(T data){
        this.status = "success";
        this.data = data;
    }
}
