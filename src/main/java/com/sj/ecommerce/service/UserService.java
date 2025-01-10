package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.helper.Response;
import com.sj.ecommerce.dto.UserDto;


public interface UserService {

    Response<UserDto> saveUser(UserDto userDto);

    Response<UserDto> getUserProfile(String id);

    Response<UserDto> updateUserProfile(String id, UserDto userDTO);

    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    Response<String> deleteUser(String userId);



    }
