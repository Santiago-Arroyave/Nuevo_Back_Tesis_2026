package Back_Goblink_park.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RolResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private Boolean estado;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}