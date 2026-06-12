# Spring Boot Inventory API

API REST para gestion de usuarios, productos e inventario, desarrollada con Java 17 y Spring Boot. El proyecto usa arquitectura por capas, DTOs, validaciones, manejo global de excepciones, documentacion con Swagger/OpenAPI, base de datos H2 en memoria y pruebas con JUnit, Mockito y `@DataJpaTest`.

Este repositorio esta pensado como proyecto de portafolio Java Junior, mostrando buenas practicas basicas de desarrollo backend con Spring Boot.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Jakarta Validation
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- Mockito
- AssertJ

## Arquitectura

El proyecto sigue una arquitectura por capas:

```text
Controller -> Service -> Repository -> Model -> Database
```

- `controller`: expone los endpoints REST.
- `service`: contiene la logica de negocio.
- `repository`: accede a la base de datos con Spring Data JPA.
- `model`: contiene las entidades JPA.
- `dto`: define objetos de entrada y salida de la API.
- `exception`: centraliza excepciones personalizadas y respuestas de error.
- `config`: contiene configuracion adicional, como OpenAPI.

## Estructura

```text
src
|-- main
|   |-- java/com/jorge/inventoryapi
|   |   |-- config
|   |   |   `-- OpenApiConfig.java
|   |   |-- controller
|   |   |   |-- MovimientoInventarioController.java
|   |   |   |-- ProductoController.java
|   |   |   `-- UsuarioController.java
|   |   |-- dto
|   |   |   |-- ErrorResponse.java
|   |   |   |-- MovimientoInventarioRequest.java
|   |   |   |-- MovimientoInventarioResponse.java
|   |   |   |-- ProductoPageResponse.java
|   |   |   |-- ProductoRequest.java
|   |   |   |-- ProductoResponse.java
|   |   |   |-- UsuarioRequest.java
|   |   |   `-- UsuarioResponse.java
|   |   |-- exception
|   |   |   |-- EmailDuplicadoException.java
|   |   |   |-- GlobalExceptionHandler.java
|   |   |   |-- ProductoNoEncontradoException.java
|   |   |   |-- StockInsuficienteException.java
|   |   |   `-- UsuarioNoEncontradoException.java
|   |   |-- model
|   |   |   |-- MovimientoInventario.java
|   |   |   |-- Producto.java
|   |   |   |-- TipoMovimiento.java
|   |   |   `-- Usuario.java
|   |   |-- repository
|   |   |   |-- MovimientoInventarioRepository.java
|   |   |   |-- ProductoRepository.java
|   |   |   `-- UsuarioRepository.java
|   |   |-- service
|   |   |   |-- MovimientoInventarioService.java
|   |   |   |-- ProductoService.java
|   |   |   `-- UsuarioService.java
|   |   `-- DemoApplication.java
|   `-- resources
|       |-- application.properties
|       `-- data.sql
`-- test
    `-- java/com/jorge/inventoryapi
        |-- DemoApplicationTests.java
        |-- repository
        |   |-- MovimientoInventarioRepositoryTest.java
        |   |-- ProductoRepositoryTest.java
        |   `-- UsuarioRepositoryTest.java
        `-- service
            |-- MovimientoInventarioServiceTest.java
            |-- ProductoServiceTest.java
            `-- UsuarioServiceTest.java
```

## Endpoints

### Usuarios

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/usuarios` | Lista todos los usuarios |
| `GET` | `/usuarios/{id}` | Busca un usuario por id |
| `GET` | `/usuarios/email/{email}` | Busca un usuario por email |
| `POST` | `/usuarios` | Crea un usuario |
| `PUT` | `/usuarios/{id}` | Actualiza un usuario |
| `DELETE` | `/usuarios/{id}` | Elimina un usuario |

### Productos

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/productos` | Lista todos los productos |
| `GET` | `/productos/{id}` | Busca un producto por id |
| `POST` | `/productos` | Crea un producto |
| `PUT` | `/productos/{id}` | Actualiza un producto |
| `DELETE` | `/productos/{id}` | Elimina un producto |

### Busquedas de productos

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/productos/categoria/{categoria}` | Busca productos por categoria sin distinguir mayusculas |
| `GET` | `/productos/buscar?nombre=mouse` | Busca productos por nombre parcial |

### Paginacion

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/productos/page?page=0&size=5` | Lista productos paginados |

La primera pagina es `0`.

### Movimientos de inventario

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/movimientos` | Lista el historial de movimientos |
| `POST` | `/movimientos` | Registra una entrada o salida de inventario |

Reglas principales:

- `ENTRADA`: aumenta el stock del producto.
- `SALIDA`: reduce el stock del producto.
- Si una salida supera el stock disponible, la API responde `409 Conflict`.

## Ejemplos JSON

### Crear usuario

```http
POST /usuarios
```

```json
{
  "nombre": "Carlos Perez",
  "email": "carlos@mail.com"
}
```

Respuesta:

```json
{
  "id": 3,
  "nombre": "Carlos Perez",
  "email": "carlos@mail.com"
}
```

### Crear producto

```http
POST /productos
```

```json
{
  "nombre": "Monitor Samsung",
  "descripcion": "Monitor 24 pulgadas Full HD",
  "stock": 15,
  "precio": 650000.00,
  "categoria": "Tecnologia"
}
```

Respuesta:

```json
{
  "id": 3,
  "nombre": "Monitor Samsung",
  "descripcion": "Monitor 24 pulgadas Full HD",
  "stock": 15,
  "precio": 650000.00,
  "categoria": "Tecnologia"
}
```

### Registrar entrada de inventario

```http
POST /movimientos
```

```json
{
  "productoId": 1,
  "tipoMovimiento": "ENTRADA",
  "cantidad": 5,
  "observacion": "Compra inicial de inventario"
}
```

Respuesta:

```json
{
  "id": 1,
  "productoId": 1,
  "productoNombre": "Mouse Logitech",
  "tipoMovimiento": "ENTRADA",
  "cantidad": 5,
  "fecha": "2026-06-12T10:30:00",
  "observacion": "Compra inicial de inventario",
  "stockActual": 25
}
```

### Registrar salida de inventario

```http
POST /movimientos
```

```json
{
  "productoId": 1,
  "tipoMovimiento": "SALIDA",
  "cantidad": 3,
  "observacion": "Venta de producto"
}
```

Respuesta:

```json
{
  "id": 2,
  "productoId": 1,
  "productoNombre": "Mouse Logitech",
  "tipoMovimiento": "SALIDA",
  "cantidad": 3,
  "fecha": "2026-06-12T10:35:00",
  "observacion": "Venta de producto",
  "stockActual": 22
}
```

### Error por stock insuficiente

```http
POST /movimientos
```

```json
{
  "productoId": 1,
  "tipoMovimiento": "SALIDA",
  "cantidad": 999,
  "observacion": "Salida mayor al stock disponible"
}
```

Respuesta esperada:

```json
{
  "timestamp": "2026-06-12T10:40:00",
  "status": 409,
  "error": "Conflict",
  "message": "Stock insuficiente para el producto con id: 1. Stock actual: 22, cantidad solicitada: 999",
  "validationErrors": null
}
```

### Error de validacion

```http
POST /productos
```

```json
{
  "nombre": "",
  "descripcion": "Producto invalido",
  "stock": -5,
  "precio": -1000,
  "categoria": ""
}
```

Respuesta esperada:

```json
{
  "timestamp": "2026-06-12T10:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud tiene campos invalidos",
  "validationErrors": {
    "nombre": "El nombre es obligatorio",
    "stock": "El stock no puede ser negativo",
    "precio": "El precio no puede ser negativo",
    "categoria": "La categoria es obligatoria"
  }
}
```

## Ejecutar el proyecto

Prerrequisitos:

- JDK 17 instalado.
- Maven instalado o Maven Wrapper incluido en el proyecto.

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Swagger UI

Abrir:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Swagger permite visualizar y probar los endpoints de usuarios, productos, busquedas, paginacion y movimientos de inventario.

## H2 Console

Abrir:

```text
http://localhost:8080/h2-console
```

Datos de conexion:

```text
JDBC URL: jdbc:h2:mem:usuariosdb
User Name: sa
Password:
```

El campo `Password` debe quedar vacio.

Consultas utiles:

```sql
SELECT * FROM USUARIOS;
SELECT * FROM PRODUCTOS;
SELECT * FROM MOVIMIENTOS_INVENTARIO;
```

## Ejecutar tests

Ejecutar toda la suite:

```powershell
.\mvnw.cmd test
```

Ejecutar una clase especifica:

```powershell
.\mvnw.cmd test "-Dtest=MovimientoInventarioServiceTest"
```

Desde IntelliJ IDEA:

- Clic derecho sobre `src/test/java` para ejecutar todos los tests.
- Clic derecho sobre una clase de test para ejecutar solo esa clase.
- Clic derecho sobre un metodo de test para ejecutar un caso puntual.

## Pruebas implementadas

La suite actual incluye 23 tests:

- `DemoApplicationTests`: valida que el contexto de Spring Boot cargue correctamente.
- Repository tests con `@DataJpaTest`:
  - `ProductoRepositoryTest`
  - `UsuarioRepositoryTest`
  - `MovimientoInventarioRepositoryTest`
- Service tests con Mockito:
  - `ProductoServiceTest`
  - `UsuarioServiceTest`
  - `MovimientoInventarioServiceTest`

Los repository tests usan `spring.sql.init.mode=never` para no depender de `data.sql`. Cada test crea sus propios datos, lo que mejora aislamiento y repetibilidad.

## JUnit, Mockito y @DataJpaTest

JUnit 5 es el framework usado para definir y ejecutar pruebas en Java mediante anotaciones como `@Test` y `@DisplayName`.

Mockito permite probar services sin conectarse a una base de datos real. Con `@Mock` se crean dependencias simuladas, con `@InjectMocks` se inyectan en el service real, con `when(...).thenReturn(...)` se define el comportamiento esperado y con `verify(...)` se confirma que una dependencia fue llamada correctamente.

`@DataJpaTest` levanta una porcion reducida del contexto de Spring enfocada en JPA: entidades, repositories, Hibernate y una base H2 embebida. No carga controllers ni services, por eso es util para probar repositories de forma rapida.

## Estado de Swagger

- Los controllers tienen `@Tag`, `@Operation`, `@ApiResponse` y `@ApiResponses`.
- Los DTOs principales tienen `@Schema`.
- Los endpoints de usuarios, productos, busquedas, paginacion y movimientos de inventario quedan visibles en Swagger UI.
- `MovimientoInventarioController` esta expuesto bajo `/movimientos`.

## Proximas mejoras sugeridas

- Agregar perfiles separados: `dev`, `test` y `prod`.
- Agregar tests de controllers con `@WebMvcTest`.
- Agregar filtros avanzados de productos.
- Agregar auditoria basica de fechas de creacion y actualizacion.
- Agregar documentacion de ejemplos de respuesta en OpenAPI.
- Agregar autenticacion con JWT en una fase posterior.
- Agregar Docker en una fase posterior.

## Commits sugeridos

- `Add inventory movement module`
- `Add product search and pagination`
- `Add repository and service tests`
- `Update Swagger documentation`
- `Update README for portfolio presentation`

## Autor

Jorge
