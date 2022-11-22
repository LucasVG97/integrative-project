package com.example.finalproject.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
public class PageableResponseDTO {
    private Collection<?> content;

    private int size;
    private int totalPages;

    private Long totalSellers;


    public PageableResponseDTO toResponse(Page<?> page){
        this.content = page.getContent();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalSellers = page.getTotalElements();

        return this;
    }
}
