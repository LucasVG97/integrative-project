package com.example.finalproject.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SellerRequestDTO {

    @NotNull
    private String name;

    @NotNull
    @Email(message = "Email must be in this format: example@example.com")
    private String email;

}
