/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.exceptions;

import com.example.app.user.exceptions.FieldAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.time.Instant;

/**
 *
 * @author rashi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleFieldAlreadyExists(FieldAlreadyExistsException ex) {
        Map<String, Object> body = Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.CONFLICT.value(),
            "error", "Conflict",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
