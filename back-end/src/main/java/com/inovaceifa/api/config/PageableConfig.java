package com.inovaceifa.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PageableConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver();

        // ❌ REMOVIDA ORDENAÇÃO GLOBAL FIXA
        pageableResolver.setFallbackPageable(
                PageRequest.of(0, 10)
        );

        pageableResolver.setMaxPageSize(50);

        resolvers.add(pageableResolver);
    }
}