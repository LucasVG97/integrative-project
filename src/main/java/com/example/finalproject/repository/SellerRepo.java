package com.example.finalproject.repository;

import com.example.finalproject.dto.SellerDTO;
import com.example.finalproject.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, Long>{

    Page<SellerDTO> findAllByRating(String rating, Pageable pageable);

}
