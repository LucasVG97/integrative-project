package com.example.finalproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "advertisement_code")
    @JsonIgnore
    private Advertisement advertisement;

    @NotNull
    @Column(nullable = false)
    private Float currentTemperature;

    @NotNull
    @Column(nullable = false)
    private int productQuantity;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime manufacturingDateTime;

    @NotNull
    @Column(nullable = false)
    private Float volume;

    @NotNull
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_code")
    @JsonIgnoreProperties("batchStock")
    @JsonIgnore
    private InboundOrder inboundOrder;

    public Float getUnitVolume() {
        return getVolume() / getProductQuantity();
    }
}
