package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "proyecto_reportes",

        uniqueConstraints = {

                @UniqueConstraint(
                        columnNames = {
                                "id_proyecto",
                                "id_reporte"
                        }
                )
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoReporte {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_proyecto_reporte")

    private Long id;

    // =====================================================
    // RELACIÓN PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_proyecto")

    private Proyecto proyecto;

    // =====================================================
    // RELACIÓN REPORTE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_reporte")

    private Reporte reporte;

    // =====================================================
    // FECHA ASOCIACIÓN
    // =====================================================

    private LocalDateTime fechaAsociacion;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaAsociacion = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();
    }
}