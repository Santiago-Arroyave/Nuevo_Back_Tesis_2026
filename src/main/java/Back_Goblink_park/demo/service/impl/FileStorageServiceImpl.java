package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${supabase.url:${SUPABASE_URL:https://znkoxmkxkimteknuacya.supabase.co}}")
    private String supabaseUrl;

    @Value("${supabase.key:${SUPABASE_KEY:${SUPABASE_SERVICE_ROLE_KEY:${SUPABASE_ANON_KEY:}}}}")
    private String supabaseKey;

    @Value("${supabase.storage.bucket:${SUPABASE_STORAGE_BUCKET:goblin-media}}")
    private String bucketName;

    private final RestClient restClient;

    public FileStorageServiceImpl() {
        this.restClient = RestClient.builder().build();
    }

    @PostConstruct
    public void init() {
        if (supabaseKey == null || supabaseKey.isBlank()) {
            log.warn("⚠️ [FileStorageService] SUPABASE_KEY no está configurada o está vacía. Es requerida para subir archivos a Supabase Storage.");
        } else {
            log.info("✅ [FileStorageService] Configurado correctamente para bucket '{}' en {}", bucketName, supabaseUrl);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType() != null ? file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
            return uploadBytes(fileBytes, originalFilename, contentType, folder);
        } catch (IOException e) {
            log.error("Error al leer los bytes del archivo para subir: {}", e.getMessage(), e);
            throw new BusinessException("Error al procesar el archivo: " + e.getMessage());
        }
    }

    public String uploadBytes(byte[] bytes, String originalFilename, String contentType, String folder) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        String key = (supabaseKey != null) ? supabaseKey.trim() : "";
        if (key.isBlank()) {
            log.error("Clave de Supabase no configurada (SUPABASE_KEY). Imposible subir archivo a Supabase Storage.");
            throw new BusinessException("Configuración de Supabase Storage incompleta: SUPABASE_KEY no está definida en las variables de entorno.");
        }

        String cleanFolder = (folder != null) ? folder.trim().replaceAll("^/+|/+$", "") : "general";
        if (cleanFolder.isBlank()) {
            cleanFolder = "general";
        }

        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else if (contentType != null) {
            if (contentType.contains("jpeg") || contentType.contains("jpg")) extension = ".jpg";
            else if (contentType.contains("png")) extension = ".png";
            else if (contentType.contains("webp")) extension = ".webp";
            else if (contentType.contains("pdf")) extension = ".pdf";
        }

        String uniqueFileName = UUID.randomUUID() + extension;
        String objectPath = cleanFolder + "/" + uniqueFileName;
        String cleanBaseUrl = (supabaseUrl != null) ? supabaseUrl.trim().replaceAll("/+$", "") : "";
        String uploadEndpoint = cleanBaseUrl + "/storage/v1/object/" + bucketName + "/" + objectPath;

        log.info("Subiendo archivo a Supabase Storage: endpoint={}, size={} bytes", uploadEndpoint, bytes.length);

        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri(uploadEndpoint)
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .header("apiKey", key)
                    .header("apikey", key)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .header("x-upsert", "true")
                    .body(bytes)
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode().is2xxSuccessful()) {
                String publicUrl = cleanBaseUrl + "/storage/v1/object/public/" + bucketName + "/" + objectPath;
                log.info("Archivo subido exitosamente. URL pública: {}", publicUrl);
                return publicUrl;
            } else {
                throw new BusinessException("Respuesta inesperada al subir archivo: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error al subir archivo a Supabase Storage: {}", e.getMessage(), e);
            throw new BusinessException("Error al subir archivo a Supabase Storage: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        String marker = "/storage/v1/object/public/" + bucketName + "/";
        int index = fileUrl.indexOf(marker);
        if (index == -1) {
            log.debug("La URL {} no pertenece al bucket {}, no se eliminará de Supabase Storage", fileUrl, bucketName);
            return;
        }

        String key = (supabaseKey != null) ? supabaseKey.trim() : "";
        if (key.isBlank()) {
            log.warn("No se puede eliminar el archivo {} de Supabase Storage: SUPABASE_KEY no configurada", fileUrl);
            return;
        }

        String objectPath = fileUrl.substring(index + marker.length());
        String cleanBaseUrl = (supabaseUrl != null) ? supabaseUrl.trim().replaceAll("/+$", "") : "";
        String deleteEndpoint = cleanBaseUrl + "/storage/v1/object/" + bucketName + "/" + objectPath;

        try {
            restClient.delete()
                    .uri(deleteEndpoint)
                    .header("apiKey", key)
                    .header("apikey", key)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Archivo eliminado correctamente de Supabase Storage: {}", objectPath);
        } catch (Exception ex) {
            log.warn("No se pudo eliminar el archivo {} de Supabase Storage: {}", objectPath, ex.getMessage());
        }
    }
}
