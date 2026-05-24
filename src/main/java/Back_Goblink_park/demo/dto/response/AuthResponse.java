package Back_Goblink_park.demo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;

    private UserResponse usuario;
}