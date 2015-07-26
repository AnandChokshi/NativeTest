package com.inventure.test.nativetest.model;

/**
 * Created by Anand on 6/25/2015.
 */
public class Validation {

    private int required;
    private int server_validation;
    private String validation_type;
    private String regex;
    private String error_message;

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getServer_validation() {
        return server_validation;
    }

    public void setServer_validation(int server_validation) {
        this.server_validation = server_validation;
    }

    public String getValidation_type() {
        return validation_type;
    }

    public void setValidation_type(String validation_type) {
        this.validation_type = validation_type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
