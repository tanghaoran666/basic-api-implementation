package com.thoughtworks.rslist.exception;

import lombok.Data;

@Data
public class Error {
    public void setError(String error) {
        this.error = error;
    }

    private String error;

}
