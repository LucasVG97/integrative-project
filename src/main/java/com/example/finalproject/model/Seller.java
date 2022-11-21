package com.example.finalproject.model;

import com.example.finalproject.dto.SellerRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Email(message = "Email must be in this format: example@example.com")
    private String email;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"seller", "purchaseItems"})
    private List<Advertisement> advertisements;


    public Seller(SellerRequestDTO sellerRequestDTO){
        this.name = sellerRequestDTO.getName();
        this.email = sellerRequestDTO.getEmail();
    }
}
