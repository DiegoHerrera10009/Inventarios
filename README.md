# Sistema de Inventario de Repuestos

## ¿Qué es el proyecto?

Es un **sistema web de inventario** pensado para gestionar repuestos (automotrices, generadores, etc.): dar de alta productos, consultar stock, registrar entradas y salidas, y ver alertas de stock bajo. Incluye **registro e inicio de sesión** para que solo usuarios registrados usen el sistema.

---

## ¿Con qué está hecho? (Stack tecnológico)

| Aspecto | Tecnología |
|--------|------------|
| **Lenguaje** | Java 17 |
| **Framework** | Spring Boot 3.4.4 |
| **Backend** | Spring MVC (controladores), Spring Data JPA (persistencia) |
| **Base de datos** | PostgreSQL |
| **Frontend / vistas** | Thymeleaf (plantillas HTML en el servidor) |
| **Estilos** | CSS propio (`industrial.css`) |
| **Build / dependencias** | Maven |
| **APIs de persistencia** | Jakarta Persistence (JPA), Hibernate |
| **Validación** | Jakarta Validation (en dependencias) |

No usa JavaScript framework (React, Vue, etc.): todo se renderiza en el servidor con Thymeleaf y se envían formularios clásicos (GET/POST).

---

## ¿En qué arquitectura está?

Es una aplicación **en capas (layered)** tipo **MVC en el servidor**:

1. **Capa de presentación**
   - **Controladores** (`Controladores/`): reciben peticiones HTTP, llaman a servicios y devuelven nombres de vistas o redirecciones.
   - **Vistas**: plantillas Thymeleaf en `resources/templates/` (HTML) y CSS en `resources/static/css/`.

2. **Capa de lógica de negocio**
   - **Servicios** (`Servicios/`): reglas de negocio (validar código duplicado, stock mínimo, registrar salidas, etc.). Los controladores solo hablan con servicios, no con repositorios (salvo un caso en salidas).

3. **Capa de persistencia**
   - **Entidades** (`Persistencia/Entidades/`): clases JPA que se mapean a tablas en PostgreSQL.
   - **Repositorios** (`Persistencia/Repositorio/`): interfaces Spring Data JPA (JpaRepository) para acceder a la base de datos sin escribir SQL a mano.

4. **Seguridad / filtrado**
   - **AuthInterceptor**: intercepta todas las peticiones; si no hay sesión con usuario logueado, redirige a login. Rutas públicas: `/`, `/login`, `/registro`, `/error` y recursos estáticos (CSS, JS, imágenes).

Flujo típico: **Navegador → Controlador → Servicio → Repositorio → Base de datos**, y la respuesta vuelve como **vista Thymeleaf** (HTML) o **redirect**.

---

## Estructura del proyecto (paquetes y carpetas)

```
Inventarios/
├── pom.xml                          # Maven: dependencias y versión de Java
├── src/main/
│   ├── java/co/edu/ucentral/inventario_data/
│   │   ├── InventarioApplication.java     # Punto de entrada Spring Boot
│   │   ├── AuthInterceptor.java           # Filtro de login por sesión
│   │   ├── WebConfig.java                 # Configuración: interceptor + carpeta uploads
│   │   ├── Controladores/
│   │   │   ├── UsuarioControlador.java   # Inicio, registro, login, menú, logout
│   │   │   ├── ProductoControlador.java  # CRUD producto, lista, ficha, entrada stock
│   │   │   └── SalidaProductoControlador.java  # Formulario salida, historial
│   │   ├── Servicios/
│   │   │   ├── UsuarioServicio.java
│   │   │   ├── ProductoServicio.java
│   │   │   └── SalidaProductoServicio.java
│   │   └── Persistencia/
│   │       ├── Entidades/
│   │       │   ├── Usuario.java
│   │       │   ├── Producto.java
│   │       │   └── SalidaProducto.java
│   │       └── Repositorio/
│   │           ├── UsuarioRepositorio.java
│   │           ├── ProductoRepositorio.java
│   │           └── SalidaProductoRepositorio.java
│   └── resources/
│       ├── application.properties        # Puerto, BD, JPA, Thymeleaf, multipart
│       ├── templates/                    # Vistas Thymeleaf (.html)
│       └── static/css/industrial.css    # Estilos
└── src/test/
    └── java/.../InventarioApplicationTests.java
```

---

## Modelo de datos (entidades y relaciones)

- **Usuario**: id, nombre, contraseña, correo. Se usa para login y sesión.
- **Producto**: id, código (referencia única), nombre, marca, categoría, tipo de equipo, aplicación vehículo, ubicación en bodega, imagen (nombre de archivo), descripción, cantidad (stock), stock mínimo, precio.
- **SalidaProducto**: id, producto (relación ManyToOne con Producto), cantidad, motivo, fecha. Registra cada salida de stock.

La base de datos se crea/actualiza con **Hibernate** (`ddl-auto=update`) según las entidades JPA.

---

## Funcionalidades (qué puede hacer el usuario)

1. **Público (sin login)**  
   - Ver página de inicio.  
   - Registrarse (nombre, contraseña, correo).  
   - Iniciar sesión.

2. **Con sesión iniciada**  
   - Menú principal.  
   - **Productos**: crear producto (con imagen opcional), listar, buscar por referencia/nombre, ver ficha, ver lista de stock bajo, dar entrada de stock.  
   - **Salidas**: elegir producto y cantidad, indicar motivo, registrar salida (se descuenta stock); ver historial de salidas ordenado por fecha.  
   - Cerrar sesión (logout).

La autenticación es **basada en sesión HTTP**: al hacer login se guarda el `Usuario` en la sesión (`usuarioLogueado`); el `AuthInterceptor` comprueba que exista antes de dejar pasar a rutas protegidas.

---

## Configuración importante (application.properties)

- **Servidor**: puerto **8862**.  
- **Base de datos**: PostgreSQL en `localhost:5432`, base `inventario_data`, usuario y contraseña configurados ahí.  
- **JPA**: `ddl-auto=update`, dialecto PostgreSQL, SQL formateado y mostrado en consola (útil en desarrollo).  
- **Thymeleaf**: caché desactivada en desarrollo.  
- **Subida de archivos**: máximo 10 MB para la imagen del repuesto.  
- **Recursos**: las imágenes subidas se sirven desde la carpeta `uploads/` vía `WebConfig` (`/uploads/**`).

---

## Cómo se hizo (resumen para explicar)

- Se eligió **Spring Boot** para tener servidor web, JPA y Thymeleaf integrados con poco código de configuración.  
- Se usó **Java 17** y **Spring Boot 3.x** con **Jakarta** (no javax).  
- La arquitectura es **en capas**: controladores → servicios → repositorios → BD; las vistas son Thymeleaf en el servidor.  
- La seguridad es **custom**: interceptor que comprueba sesión, sin Spring Security completo; login/registro con formularios que envían a controladores que validan contra la BD.  
- Los **repositorios** son interfaces de Spring Data JPA; las consultas específicas (por código, stock bajo, historial de salidas) se definen por nombre de método o con `@Query`.  
- Las **imágenes** de productos se guardan en disco en `uploads/` con nombre único (UUID + nombre original) y se asocia solo el nombre del archivo en la entidad `Producto`.

Con esto, alguien puede entender **qué es el proyecto, con qué está hecho, en qué arquitectura está, qué lenguajes y tecnologías usa, y cómo está organizado** para poder explicarlo o mantenerlo.
