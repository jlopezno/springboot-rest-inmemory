# API REST de Gestión de Usuarios

Este proyecto es una API REST simple desarrollada con Spring Boot y Java 11 para la gestión básica de usuarios. Utiliza una base de datos en memoria (`ArrayList`), por lo que los datos se reinician con cada ejecución.

## 🛠️ Tecnologías Utilizadas
*   **Java 11**
*   **Spring Boot**
*   **Maven**
*   **Controlador REST y Servicios**

## 🚀 Instrucciones de Uso (Cómo ejecutarlo)

Puedes clonar este repositorio y ejecutarlo localmente.

### Prerrequisitos
*   **JDK 11** o superior instalado.
*   **Maven** instalado.
*   Un IDE como IntelliJ IDEA, Eclipse o VS Code.

### Pasos
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com
    ```
2.  **Abrir en tu IDE** e importar como proyecto Maven.
3.  **Ejecutar la clase principal** que contiene `@SpringBootApplication`.
4.  La API estará disponible en `http://localhost:8080`.

## 📌 Endpoints de la API

| Método | Endpoint | Descripción | Ejemplo de Uso (Postman/Navegador) |
| :--- | :--- | :--- | :--- |
| **GET** | `/usuarios` | Lista todos los usuarios. | Abre el navegador y ve a `http://localhost:8080/usuarios` |
| **POST** | `/usuarios` | Crea un nuevo usuario. | Usa Postman con un cuerpo JSON: `{"nombre": "Nuevo", "email": "nuevo@mail.com"}` |

### Ejemplo de uso con PowerShell (POST)

Puedes crear un nuevo usuario usando tu comando en PowerShell:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/usuarios -Method POST -ContentType "application/json" -Body '{"id":4,"nombre":"Luis","email":"luis@mail.com"}'
