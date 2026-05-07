package com.inovaceifa.api.mapper;

import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import org.springframework.data.domain.Page;

public final class PageMapper {

    private PageMapper() {
    }

    public static <T> PageResponseDTO<T> toPageResponse(Page<T> page) {

        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .last(page.isLast())
                .build();
    }
}
