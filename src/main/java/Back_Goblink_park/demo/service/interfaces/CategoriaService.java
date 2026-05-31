package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.CategoriaRequest;
import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoriaService {

    // Crear categoría
    CategoriaResponse crear(CategoriaRequest request, MultipartFile imagen);

    // Listar todas las categorías
    List<CategoriaResponse> listar();

    // Obtener categoría por ID
    CategoriaResponse obtenerPorId(Long id);

    // Actualizar categoría
    CategoriaResponse actualizar(Long id, CategoriaRequest request, MultipartFile imagen);

    // Eliminar categoría
    void eliminar(Long id);
}