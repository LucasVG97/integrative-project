package com.example.finalproject.service.impl;

import com.example.finalproject.dto.SellerRequestDTO;
import com.example.finalproject.dto.SellerResposeDTO;
import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.model.Seller;
import com.example.finalproject.repository.SellerRepo;
import com.example.finalproject.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService{

    private final SellerRepo sellerRepo;


    @Override
    public List<Seller> findAll() {
        List<Seller> sellerList = sellerRepo.findAll();
        if(sellerList.isEmpty()) throw new NotFoundException("there aren't any sellers");
        return sellerList;
    }

    @Override
    public SellerResposeDTO createSeller(SellerRequestDTO sellerRequestDTO){
        Seller seller = new Seller(sellerRequestDTO);
        seller.setSales(0L);
        seller.setRating("Not enough sales to be rated");
        sellerRepo.save(seller);
        return new SellerResposeDTO(seller);
    }

    @Override
    public Seller findBySellerCode(Long sellerCode) {
        return sellerRepo.findById(sellerCode).orElseThrow(() -> new NotFoundException("Seller with seller code: " + sellerCode + " not found"));
    }

    @Override
    public SellerResposeDTO updateSeller(SellerRequestDTO sellerRequestDTO, Long sellerCode) {
        Seller seller = sellerRepo.findById(sellerCode).orElseThrow(() -> new NotFoundException("Seller with seller code: " + sellerCode + " not found"));
        seller.setName(sellerRequestDTO.getName());
        seller.setEmail(sellerRequestDTO.getEmail());
        sellerRepo.save(seller);
        return new SellerResposeDTO(seller);
    }

}
