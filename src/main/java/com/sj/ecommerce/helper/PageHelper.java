package com.sj.ecommerce.helper;

import com.sj.ecommerce.dto.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PageHelper {

    private final ModelMapper modelMapper;

    // Constructor-based dependency injection
    public PageHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
        // Map entities to DTOs
        List<V> dtoList = page.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, type))
                .collect(Collectors.toList());

        // Adjust the page number to start from 1
        int pageNumber = page.getNumber() + 1;

        // Create and return the PageableResponse object
        return new PageableResponse<>(
                dtoList,
                pageNumber,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
