package com.opsnow.backend.utils;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PaginatedResponse<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public PaginatedResponse(Page<T> pageData) {
        this.data = pageData.getContent();
        this.page = pageData.getNumber();
        this.pageSize = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
    }
}
