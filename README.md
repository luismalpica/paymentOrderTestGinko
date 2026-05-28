# Payment Order

API REST desarrollada con Java 17 y Spring Boot 3 para la gestión de proveedores y órdenes de pago.

---

# Tecnologías utilizadas

* Java 17
* Spring Boot 3
* Spring Web
* Spring Data JPA
* Spring Validation
* Maven
* H2 Database
* Swagger / OpenAPI
* JUnit 5
* Mockito
* Lombok

---

# Requisitos previos

Antes de ejecutar el proyecto debes tener instalado:

* Java 17 o superior
* Maven 3.9 o superior
* Git (opcional)

Verificar versiones:

```bash
java -version
mvn -version
```

---

# Instrucciones para ejecutar localmente

## 1. Clonar el proyecto

```bash
git clone <url-del-repositorio>
```

## 2. Entrar al proyecto

```bash
cd payment-order-api
```

## 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación iniciará en:

```text
http://localhost:8080
```

---

# Base de datos H2

La aplicación utiliza H2 en memoria.

## Consola H2

URL:

```text
http://localhost:8080/h2-console
```

Credenciales:

```text
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password:
```

---

# URL local de Swagger

Documentación OpenAPI:

```text
http://localhost:8080/swagger-ui.html
```

También disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Instrucciones para ejecutar pruebas

## Ejecutar todas las pruebas

```bash
mvn test
```

## Ejecutar únicamente pruebas de integración

```bash
mvn test -Dtest=*IntegrationTest
```

---

# Funcionalidades implementadas

## Gestión de proveedores

* Crear proveedor
* Consultar proveedor por ID
* Listar proveedores con paginación
* Filtrar proveedores por estado
* Actualizar proveedor
* Cambiar estado ACTIVO / INACTIVO

## Gestión de órdenes de pago

* Crear orden de pago
* Validación de proveedor activo
* Validación de monto mayor a cero
* Validación de longitud del concepto
* Consultar orden por ID
* Listar órdenes con filtros
* Cambio de estados controlado
* Validación de transiciones válidas

## Calidad transversal

* Manejo global de excepciones
* Respuestas HTTP consistentes
* Validaciones con Bean Validation
* Swagger/OpenAPI
* Pruebas unitarias
* Prueba de integración
* Idempotencia mediante header `Idempotency-Key`
* Manejo básico de concurrencia con `@Version`

---

# Decisiones de diseño tomadas

## Uso de arquitectura por capas

Se separó la aplicación en:

* Controller
* Service
* Repository
* DTO
* Mapper

Esto permite:

* Mejor mantenibilidad
* Bajo acoplamiento
* Mayor facilidad para pruebas unitarias

---

## Idempotencia

La creación de órdenes utiliza el header:

```text
Idempotency-Key
```

Si una petición se repite con la misma llave, el sistema retorna la orden previamente creada y evita duplicados.

---

## Concurrencia

Se agregó control optimista utilizando:

```java
@Version
```

Esto ayuda a evitar actualizaciones simultáneas inconsistentes sobre una orden de pago.

---

## Manejo centralizado de excepciones

Se implementó `@RestControllerAdvice` para retornar respuestas consistentes:

* 400 Bad Request
* 404 Not Found
* 409 Conflict

---

## Uso de H2

Se eligió H2 por simplicidad y facilidad de ejecución local sin requerir instalación adicional de bases de datos.

# Ejemplo de endpoints

## Crear proveedor

```http
POST /api/providers
```

## Crear orden

```http
POST /api/orders
```

Header requerido:

```text
Idempotency-Key: abc-123
```

---