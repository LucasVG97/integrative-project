package com.example.finalproject.controller;

import com.example.finalproject.dto.AdvertisementDTO;
import com.example.finalproject.dto.AdvertisementOrderDTO;
import com.example.finalproject.dto.PurchaseOrderCreateDTO;
import com.example.finalproject.model.PurchaseOrder;
import com.example.finalproject.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class PurchaseOrderController {

    @Autowired
    private IPurchaseOrderService service;

    @PostMapping("/orders")
    public ResponseEntity<BigDecimal> createPurchaseOrder(@Valid @RequestBody PurchaseOrderCreateDTO purchaseOrderCreateDTO) {
        PurchaseOrder purchaseOrder = PurchaseOrderCreateDTO.convertToPurchaseOrder(purchaseOrderCreateDTO);
        List<Integer> advertisementQuantityList = purchaseOrderCreateDTO.getAdvertisementListDTO().stream().map(AdvertisementOrderDTO::getQuantity).collect(Collectors.toList());
        return new ResponseEntity<>(service.createPurchaseOrder(purchaseOrder, advertisementQuantityList), HttpStatus.CREATED);
    }
    
    @GetMapping("/orders/{purchaseCode}")
    public ResponseEntity<List<AdvertisementDTO>> findAllAdvertisementsByPurchase(@PathVariable Long purchaseCode) {
        return ResponseEntity.ok(AdvertisementDTO.convertListToResponse(service.findAllAdvertisementsByPurchase(purchaseCode)));
    }

    @PutMapping("/orders/{purchaseCode}")
    public ResponseEntity<PurchaseOrder> updatePurchaseStatus(@PathVariable Long purchaseCode) {
        return ResponseEntity.ok(service.updatePurchaseStatus(purchaseCode));
    }
}
