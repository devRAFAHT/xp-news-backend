package br.edu.ifba.xpnewsbackend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdatePasswordDto {

    @NotBlank(message = "The current password is required.")
    private String currentPassword;
    @NotBlank(message = "The new password is required.")
    private String newPassword;
    @NotBlank(message = "The confirmation password is required.")
    private String confirmationPassword;

}
