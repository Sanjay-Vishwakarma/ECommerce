package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.common.Function;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.helper.Helper;
import com.sj.ecommerce.helper.Response;
import com.sj.ecommerce.dto.UserDto;
import com.sj.ecommerce.entity.User;
import com.sj.ecommerce.repository.UserRepository;
import com.sj.ecommerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    Response<UserDto> response = new Response<>();


    @Override
    public Response<UserDto> saveUser(UserDto userDto) {
        Response<UserDto> response = new Response<>();
        if (userDto == null) {
            response.setCode("UF0000");
            response.setStatus("Failure");
            response.setMessage("User details should not be null");
            return response;
        }
        try {
            // Check if the user already exists
            Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser.isPresent()) {
                response.setCode("UF0001");
                response.setStatus("Failure");
                response.setMessage("User already exists.");
                return response;
            }
            // Map DTO to entity and save the new user
            User user = modelMapper.map(userDto, User.class);
            user.setRoles("user");
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(Function.generatePassword());
            userRepository.save(user);

            response.setCode("US0000");
            response.setStatus("Success");
            response.setMessage("User created successfully.");
        } catch (Exception e) {
            response.setCode("UE0000");
            response.setStatus("Failure");
            response.setMessage("An error occurred while saving the user.");
            // Optionally log the exception
            e.printStackTrace();
        }
        return response;
    }


    @Override
    public Response<UserDto> getUserProfile(String id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
            UserDto map = modelMapper.map(user, UserDto.class);
            response.setData(map);
            response.setMessage("User profile successfully retrieved.");
            return response;
        } catch (Exception e) {
            response.setMessage("Error getting profile: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response<UserDto> updateUserProfile(String id, UserDto userDTO) {
        Response<UserDto> response = new Response<>(); // Initialize response object locally
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
            modelMapper.map(userDTO, user);
            user.setUpdatedAt(LocalDateTime.now());
            User saved = userRepository.save(user);
            response.setData(modelMapper.map(saved, UserDto.class));
            response.setMessage("User profile successfully updated.");
        } catch (Exception e) {
            response.setMessage("Error updating profile: " + e.getMessage());
            throw new RuntimeException("Error updating profile", e);
        }

        return response;
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Determine sorting direction
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create Pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Fetch paginated data
        Page<User> userPage = userRepository.findAll(pageable);

        // Use Helper to convert Page<User> to PageableResponse<UserDto>
        return Helper.getPageableResponse(userPage, UserDto.class);
    }


    @Override
    public Response<String> deleteUser(String userId) {
        Response<String> response = new Response<>();
        try {
            // Check if the user exists
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);

                response.setStatus("success");
                response.setMessage("User deleted successfully.");
                response.setData(null); // No additional data to return
            } else {
                response.setStatus("error");
                response.setMessage("User not found.");
                response.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Use a proper logger in production
            response.setStatus("error");
            response.setMessage("An error occurred while deleting the user.");
            response.setData(null);
        }
        return response;
    }


}
