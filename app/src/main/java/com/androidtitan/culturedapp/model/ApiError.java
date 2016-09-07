package com.androidtitan.culturedapp.model;

/**
 * Created by amohnacs on 9/1/16.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Handles JSON error messages for persistence
 */
public class ApiError {

    private int code;
    private String message;
    private HashMap<String, Object> additionalProperties = new HashMap<String, Object>();

    public ApiError() {
    }

    public ApiError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError(Integer code, String message, HashMap<String, Object> additionalProperties) {
        this.code = code;
        this.message = message;
        this.additionalProperties = additionalProperties;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}