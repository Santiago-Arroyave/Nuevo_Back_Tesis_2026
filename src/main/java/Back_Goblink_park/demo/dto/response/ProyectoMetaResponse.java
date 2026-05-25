package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMetaResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    private String proyectoNombre;

    // =====================================================
    // DESCRIPCIÓN DE LA META
    // =====================================================

    private String descripcion;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}