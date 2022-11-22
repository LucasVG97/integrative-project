package com.example.finalproject.service;

import com.example.finalproject.dto.PageableResponseDTO;
import com.example.finalproject.dto.SellerRequestDTO;
import com.example.finalproject.dto.SellerResposeDTO;
import com.example.finalproject.model.Seller;

import java.util.List;

public interface ISellerService {

    /**
     * Find all sellers.
     *
     * @return A list of all the sellers in the database.
     */
    List<Seller> findAll();

    /**
     * Find a seller by seller code.
     *
     * @param sellerCode The seller code of the seller you want to find.
     * @return A Seller object.
     */
    Seller findBySellerCode(Long sellerCode);

    /**
     * Find all products with a given rating, ordered by sales, and return a pageable response.
     *
     * @param rating The rating of the product.
     * @param page The page number
     * @param size The number of items to be returned in the page.
     * @param order can be either "asc" or "desc"
     * @return A PageableResponseDTO object.
     */
    PageableResponseDTO findAllByRatingOrderedBySales(String rating, int page, int size, String order);

    /**
     * It creates a seller.
     *
     * @param sellerRequestDTO This is the request object that contains the seller requested details.
     * @return SellerResposeDTO
     */
    SellerResposeDTO createSeller(SellerRequestDTO sellerRequestDTO);

    /**
     * It updates the seller with the given sellerCode.
     *
     * @param sellerRequestDTO This is the request object that contains the seller requested details.
     * @param sellerCode The seller code of the seller to be updated.
     * @return SellerResposeDTO
     */
    SellerResposeDTO updateSeller(SellerRequestDTO sellerRequestDTO, Long sellerCode);

}
