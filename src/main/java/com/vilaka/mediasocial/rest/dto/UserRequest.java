package com.vilaka.mediasocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Field name is required!")
    private String name;

    @NotNull(message = "Field age is required!")
    private Integer age;

}
