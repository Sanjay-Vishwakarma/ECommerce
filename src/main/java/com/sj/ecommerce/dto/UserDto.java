package com.sj.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;

    @JsonIgnore
    private String password; // Exclude from serialization

    @Size(min = 3, max = 20, message = "Invalid Name !!")
    @ApiModelProperty(value = "user_name", name = "username", required = true, notes = "user name of new user !!")
    private String name;

    //    @Email(message = "Invalid User Email !!")
//    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !!")
//    @NotBlank(message = "Email is required !!")
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private Set<RoleDto> roles;

}
