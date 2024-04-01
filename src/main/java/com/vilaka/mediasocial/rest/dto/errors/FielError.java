package com.vilaka.mediasocial.rest.dto.errors;

import lombok.Data;

@Data
public class FielError {
    private String field;
    private String message;

    public FielError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
