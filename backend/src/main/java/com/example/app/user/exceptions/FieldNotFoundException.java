/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.user.exceptions;

/**
 *
 * @author rashi
 */
public class FieldNotFoundException extends RuntimeException {

    private final String field;

    public FieldNotFoundException(String field) {
        super(field + " already exists");
        this.field = field;
    }

    public String getField() {
        return field;
    }
}