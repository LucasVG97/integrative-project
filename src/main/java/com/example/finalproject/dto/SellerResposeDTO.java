package com.example.finalproject.dto;

import com.example.finalproject.model.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SellerResposeDTO {

    private Long sellerCode;

    private String name;

    private String email;

    public SellerResposeDTO(Seller seller){
        this.sellerCode = seller.getSellerCode();
        this.name = seller.getName();
        this.email = seller.getEmail();
    }
}
