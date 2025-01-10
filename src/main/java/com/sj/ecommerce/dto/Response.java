package com.sj.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private T data;
    private String code;
    private String message;
    private String status;

}

