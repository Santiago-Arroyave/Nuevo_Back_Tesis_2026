package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String nombres;

    @NotBlank
    @Email
    private String correo;

    @NotBlank
    @Size(min = 6)
    private String password;
}