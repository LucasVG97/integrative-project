package com.example.finalproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemCode;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "purchase_code")
    @JsonIgnoreProperties("purchaseItems")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "advertisement_code")
    @JsonIgnoreProperties("purchaseItems")
    private Advertisement advertisement;

}
