package com.example.finalproject.service.impl;

import com.example.finalproject.dto.SellerRequestDTO;
import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Seller;
import com.example.finalproject.repository.AdvertisementRepo;
import com.example.finalproject.repository.SellerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @InjectMocks
    SellerService sellerService;

    @Mock
    SellerRepo sellerRepo;

    @Mock
    AdvertisementRepo advertisementRepo;

    private Advertisement advertisement;

    private List<Advertisement> advertisementList;

    private Seller seller;

    private List<Seller> sellerList;

    private SellerRequestDTO sellerRequestDTO = new SellerRequestDTO();

    @BeforeEach
    void setup(){
        sellerList = new ArrayList<>();

        seller = Seller.builder()
                .email("lucas@test.com")
                .name("lucas")
                .sales(0L)
                .rating("Not enough sales to be rated")
                .build();

        sellerList.add(seller);

        sellerRequestDTO.setName("mauri");
        sellerRequestDTO.setEmail("mauri@test.com");

    }

    @Test
    void findAll_returnSellerList_whenSuccess(){
        when(sellerRepo.findAll()).thenReturn(sellerList);
        var result = sellerService.findAll();
        verify(sellerRepo, times(1)).findAll();
        Assertions.assertNotNull(result);
    }

    @Test
    void findBySellerCode_returnSeller_whenSuccess(){
        when(sellerRepo.findById(any())).thenReturn(Optional.ofNullable(seller));
        var result = sellerService.findBySellerCode(1L);
        verify(sellerRepo, times(1)).findById(any());
        Assertions.assertNotNull(result);
    }

    @Test
    void findBySellerCode_returnNotFoundException_whenSellerNotFound(){
        Long invalidSellerCode = 2L;
        Assertions.assertThrows(NotFoundException.class, () -> sellerService.findBySellerCode(invalidSellerCode));
    }

    @Test
    void createSeller_returnSeller_whenSuccess(){
        sellerService.createSeller(sellerRequestDTO);
        Seller seller = new Seller(sellerRequestDTO);
        seller.setSales(0L);
        seller.setRating("Not enough sales to be rated");
        verify(sellerRepo, times(1)).save(any());
    }

    @Test
    void updateSeller_returnSeller_whenSuccess() {
        when(sellerRepo.findById(any())).thenReturn(Optional.ofNullable(seller));
        sellerService.updateSeller(sellerRequestDTO, 1L);
        verify(sellerRepo, times(1)).save(seller);
    }

    @Test
    void findBySellerCodeUpdate_returnNotFoundException_whenSellerNotFound(){
        Long invalidSellerCode = 2L;
        Assertions.assertThrows(NotFoundException.class, () -> sellerService.updateSeller(sellerRequestDTO, invalidSellerCode));
    }

}
