package br.edu.ifba.xpnewsbackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateDto {

    @NotBlank(message = "The full name is required.")
    @Size(max = 50, message = "The full name cannot exceed 50 characters.")
    private String fullName;
    @NotBlank(message = "The username is required.")
    @Size(max = 30, message = "The username cannot exceed 30 characters.")
    private String username;
    @NotBlank(message = "The email address is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;
    @NotBlank(message = "The password is required.")
    private String password;

}
