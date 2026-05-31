package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PrioridadService {

    // =====================================================
    // CREAR
    // =====================================================
    // Cambiado para recibir la imagen
    PrioridadResponse crear(PrioridadRequest request, MultipartFile imagen);

    // =====================================================
    // ACTUALIZAR
    // =====================================================
    // Cambiado para recibir la imagen
    PrioridadResponse actualizar(Long id, PrioridadRequest request, MultipartFile imagen);

    // =====================================================
    // LISTAR
    // =====================================================
    List<PrioridadResponse> listar();

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    PrioridadResponse obtenerPorId(Long id);

    // =====================================================
    // ELIMINAR
    // =====================================================
    void eliminar(Long id);
}