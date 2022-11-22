package com.example.finalproject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SellerRequestDTO {

    @NotNull
    @ApiModelProperty(value = "seller name", example = "mauri")
    private String name;

    @NotNull
    @ApiModelProperty(value = "seller email", example = "mauri@email.com")
    @Email(message = "Email must be in this format: example@example.com")
    private String email;

}
