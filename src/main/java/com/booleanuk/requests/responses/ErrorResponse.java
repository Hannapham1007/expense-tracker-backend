package com.booleanuk.requests.responses;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse extends ApiResponse<Map<String,String>>{
    public void set(String msg){
        this.status = "error";
        Map<String, String> reply = new HashMap<>();
        reply.put("message", msg);
        this.data = reply;
    }
}
