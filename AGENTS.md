# Reglas de Agente para la Carpeta `Nuevo_Back_Tesis_2026/` (Spring Boot API)

Este directorio es administrado exclusivamente por el agente **`BACKEND`**.

## Responsabilidades
- Arquitectura Spring Boot 3.x con Java 21 (`Back_Goblink_park.demo`).
- Controladores REST (`/api/...`) para Auth, Reportes, Proyectos, Categorías, Comentarios y Dashboard.
- Seguridad robusta con Spring Security + JWT stateless.
- Persistencia relacional y geoespacial en Supabase (PostgreSQL + PostGIS).
- DTOs inmutables con Bean Validation y respuestas de error uniformes.

## Restricciones
- **PROHIBIDO** modificar interfaces de usuario o carpetas `app_tesis/` y `goblin-guardians-hub/`.
- Mantener compilación limpia con Maven (`./mvnw.cmd test-compile`).
- Garantizar que los nombres de campos en respuestas JSON sigan la convención `camelCase`.
