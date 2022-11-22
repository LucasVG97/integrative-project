package com.example.finalproject.controller;

import com.example.finalproject.dto.PageableResponseDTO;
import com.example.finalproject.dto.SellerRequestDTO;
import com.example.finalproject.dto.SellerResposeDTO;
import com.example.finalproject.dto.SellerDTO;
import com.example.finalproject.service.impl.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/fresh-products")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<SellerDTO>> findAllSellers(){
        return new ResponseEntity<>(SellerDTO.convertListToResponse(sellerService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerCode}")
    public ResponseEntity<SellerDTO> findSellerBySellerCode(@PathVariable Long sellerCode){
        return new ResponseEntity<>(SellerDTO.convertToResponse(sellerService.findBySellerCode(sellerCode)), HttpStatus.OK);
    }

    @GetMapping("/seller/sales")
    public ResponseEntity<PageableResponseDTO> findAllByRating(
            @RequestParam String rating,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "2") int size,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(sellerService.findAllByRatingOrderedBySales(rating, page, size, order), HttpStatus.OK);
    }

    @PostMapping("/seller")
    public ResponseEntity<SellerResposeDTO> createSeller(@Valid @RequestBody SellerRequestDTO sellerRequestDTO){
        return new ResponseEntity<>(sellerService.createSeller(sellerRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/seller/{sellerCode}")
    public ResponseEntity<SellerResposeDTO> updateSeller(
            @PathVariable Long sellerCode,
            @Valid @RequestBody SellerRequestDTO sellerRequestDTO) {
        return new ResponseEntity<>(sellerService.updateSeller(sellerRequestDTO, sellerCode), HttpStatus.OK);
    }

}
