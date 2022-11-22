package com.example.finalproject.dto;

import com.example.finalproject.model.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {

    private String name;

    private String rating;

    private Long sales;

    private List<AdvertisementDTO> advertisements;

    public SellerDTO(Seller seller){
        this.name = seller.getName();
        this.rating = seller.getRating();
        this.sales = seller.getSales();
        this.advertisements = AdvertisementDTO.convertListToResponse(seller.getAdvertisements());

    }

    public static SellerDTO convertToResponse(Seller seller){
        return new SellerDTO(seller);
    }

    public static List<SellerDTO> convertListToResponse(List<Seller> sellerList){
        return sellerList.stream().map(SellerDTO::convertToResponse).collect(Collectors.toList());
    }

}
