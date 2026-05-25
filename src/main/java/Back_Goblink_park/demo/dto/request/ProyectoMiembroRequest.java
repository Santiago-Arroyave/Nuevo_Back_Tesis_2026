package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMiembroRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // USUARIO
    // =====================================================

    private Long usuarioId;

    // =====================================================
    // ROL EN PROYECTO
    // =====================================================

    /*
        ADMIN
        COORDINADOR
        INVESTIGADOR
        VOLUNTARIO
        APOYO
    */

    private String rolEnProyecto;
}