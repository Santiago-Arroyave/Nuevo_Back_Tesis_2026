package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoObjetivoRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // DESCRIPCIÓN DEL OBJETIVO
    // =====================================================

    private String descripcion;
}