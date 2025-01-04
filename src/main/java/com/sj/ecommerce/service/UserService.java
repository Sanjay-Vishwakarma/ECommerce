package com.sj.ecommerce.service;

import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.dto.UserDto;

import java.util.List;

public interface UserService {

    Response<UserDto> saveUser(UserDto userDto);

    Response<UserDto> getUserProfile(String id);

    Response<UserDto> updateUserProfile(String id, UserDto userDTO);

    Response<List<UserDto>> getAllUsers();

    Response<String> deleteUser(String userId);



    }
