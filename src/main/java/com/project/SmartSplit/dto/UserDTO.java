package com.project.SmartSplit.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "age cannot be empty")
    private String age;

    @NotNull(message = "gender cannot be empty")
    private String gender;

    private List<String> groupNames;

    private List<String> roleNames;

}
