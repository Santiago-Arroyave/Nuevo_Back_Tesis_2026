package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${supabase.url:https://znkoxmkxkimteknuacya.supabase.co}")
    private String supabaseUrl;

    @Value("${supabase.key:}")
    private String supabaseKey;

    @Value("${supabase.storage.bucket:goblin-media}")
    private String bucketName;

    private final RestClient restClient;

    public FileStorageServiceImpl() {
        this.restClient = RestClient.builder().build();
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
            RestClient.RequestBodySpec requestSpec = restClient.post()
                    .uri(uploadEndpoint)
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .header("x-upsert", "true");

            if (supabaseKey != null && !supabaseKey.isBlank()) {
                requestSpec.header("Authorization", "Bearer " + supabaseKey.trim())
                        .header("apikey", supabaseKey.trim());
            }

            ResponseEntity<Void> response = requestSpec.body(bytes).retrieve().toBodilessEntity();

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

        String objectPath = fileUrl.substring(index + marker.length());
        String cleanBaseUrl = (supabaseUrl != null) ? supabaseUrl.trim().replaceAll("/+$", "") : "";
        String deleteEndpoint = cleanBaseUrl + "/storage/v1/object/" + bucketName + "/" + objectPath;

        try {
            RestClient.RequestHeadersSpec<?> requestSpec = restClient.delete().uri(deleteEndpoint);
            if (supabaseKey != null && !supabaseKey.isBlank()) {
                requestSpec.header("Authorization", "Bearer " + supabaseKey.trim())
                        .header("apikey", supabaseKey.trim());
            }
            requestSpec.retrieve().toBodilessEntity();
            log.info("Archivo eliminado correctamente de Supabase Storage: {}", objectPath);
        } catch (Exception ex) {
            log.warn("No se pudo eliminar el archivo {} de Supabase Storage: {}", objectPath, ex.getMessage());
        }
    }
}
