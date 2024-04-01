package com.vilaka.mediasocial.rest.dto.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;
    private String message;
    private Collection<FielError> erros;

    public ResponseError(String message, Collection<FielError> erros) {
        this.message = message;
        this.erros = erros;
    }

    public static <T> ResponseError createErrorFromValidation(
            Set<ConstraintViolation<T>> constraintViolations){

        List<FielError> listErrors = constraintViolations
                .stream()
                .map(cv -> new FielError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation Error";

        return new ResponseError(message, listErrors);

    }

    public Response withStatusCode(int code){
        return Response.status(code).entity(this).build();
    }
}
