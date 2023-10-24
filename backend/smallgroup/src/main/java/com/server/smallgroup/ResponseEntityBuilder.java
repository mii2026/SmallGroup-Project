package com.server.smallgroup;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseEntityBuilder {
    public static ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("reason",message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> buildBindingErrorResponse(BindingResult bindingResult){
        StringBuilder errorMessages = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessages.append(error.getDefaultMessage()).append("\n");
        }
        return ResponseEntityBuilder.buildErrorResponse(errorMessages.toString(), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("result", "SUCCESS");
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponseWithInfo(HttpStatus status, String Name, Object info){
        Map<String, Object> response = new HashMap<>();
        response.put("result", "SUCCESS");
        response.put(Name, info);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponseWithInfos(HttpStatus status, String Name, List<?> info){
        Map<String, Object> response = new HashMap<>();
        response.put("result", "SUCCESS");
        for(int i = 0; i < info.size(); i++)
            response.put(Name+i, info.get(i));
        response.put("count", info.size());
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponseWithPages(HttpStatus status, Page<?> info){
        Map<String, Object> response = new HashMap<>();
        response.put("result", "SUCCESS");
        List<?> list = info.getContent();
        for(int i = 0; i < list.size(); i++)
            response.put("group"+i, list.get(i));
        response.put("count", list.size());
        response.put("pageable", info.getPageable());
        return ResponseEntity.status(status).body(response);
    }
}
