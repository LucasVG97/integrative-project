package com.example.finalproject.service;

import com.example.finalproject.dto.SellerRequestDTO;
import com.example.finalproject.dto.SellerResposeDTO;
import com.example.finalproject.model.Seller;

import java.util.List;

public interface ISellerService {

    List<Seller> findAll();

    SellerResposeDTO createSeller(SellerRequestDTO sellerRequestDTO);

    Seller findBySellerCode(Long sellerCode);

    SellerResposeDTO updateSeller(SellerRequestDTO sellerRequestDTO, Long sellerCode);
}
