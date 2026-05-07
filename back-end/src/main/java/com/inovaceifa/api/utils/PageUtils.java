package com.inovaceifa.api.utils;

import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.mapper.PageMapper;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageUtils {

    private PageUtils() {
    }

    public static <T, R> PageResponseDTO<R> toPageResponse(
            Page<T> page,
            Function<T, R> mapper
    ) {

        Page<R> mapped = page.map(mapper);

        return PageMapper.toPageResponse(mapped);
    }

}