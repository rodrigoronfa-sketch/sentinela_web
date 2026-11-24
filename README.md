# SENTINELA · Seguridad Digital

Sistema web **Java JSP + Servlet + JDBC/MySQL** inspirado en el programa adjunto
(sistema con login por tipos de usuario, menú de mantenimientos y panel de
administrador), pero adaptado al **control de asistencias** del personal.

Todo lo que necesitas está en esta carpeta: código Java, JSP, CSS, imágenes SVG,
el script SQL para XAMPP y este manual.

---

## 1. Base de datos (XAMPP / phpMyAdmin)

1. Abre **XAMPP Control Panel** y activa **Apache** y **MySQL**.
2. Entra a <http://localhost/phpmyadmin>.
3. Selecciona la pestaña **SQL** (o crea una base llamada `seguridad` y entra en ella).
4. Copia y pega todo el contenido de [`database/seguridad.sql`](database/seguridad.sql)
   y haz clic en **Continuar / Ejecutar**.

Eso crea la base de datos `seguridad` con 5 tablas:

| Tabla | Para qué sirve |
|---|---|
| `roles` | Los dos perfiles: `ADMIN` y `PERSONAL` |
| `usuarios` | Credenciales y datos del personal (aquí están `adm` y `per`) |
| `asistencias` | Registro de entrada y salida del personal (fecha, horas, estado y observaciones) |
| `activos` | Servidores, equipos, aplicaciones y cuentas vigiladas |
| `auditoria` | Registro de todas las acciones del sistema |

La conexión JDBC está en
`src/java/com/sentinel/util/ConexionBD.java`:

```java
URL  = jdbc:mysql://localhost:3306/seguridad?...
USER = root
PASS = ""        // XAMPP viene sin contraseña
```

Si tu MySQL tiene contraseña, cambia solo `USER` / `PASS` en ese archivo.

---

## 2. Usuarios y contraseñas (están en la base de datos)

| Usuario | Contraseña | Rol | Nombre |
|---|---|---|---|
| `adm` | `adm123` | Administrador | Ana Administradora |
| `per` | `per123` | Personal de seguridad | Pedro Especialista |

> También se crean `luz / luz123` y `jorge / jorge123` (PERSONAL) para que
> tengas datos de ejemplo al administrar usuarios.

### Funciones diferentes por rol

| Función | Administrador (`adm`) | Personal (`per`) |
|---|---|---|
| Ver panel con estadísticas | ✅ | ✅ (sus datos) |
| Crear asistencias | ✅ | ✅ |
| Ver asistencias | Todas | Solo las suyas |
| Editar asistencias | ✅ | Solo las suyas |
| Eliminar asistencias | ✅ | ❌ |
| Crear / editar / eliminar activos | ✅ | ❌ (solo lectura) |
| Gestionar usuarios (alta, edición, activar/desactivar) | ✅ | ❌ |
| Ver auditoría | De todos | Solo la propia |
| Cambiar su propia contraseña | ✅ | ✅ |

---

## 3. Ejecutar el proyecto Java Web

### Opción A: NetBeans con Maven (recomendada)

1. **File → Open Project…** y selecciona la carpeta `SeguridadDigital`.
   (NetBeans detecta el `pom.xml` como proyecto Maven web.)
2. Agrega un servidor **Apache Tomcat 9** si aún no lo tienes
   (*Services → Servers → Add Server*). El código usa `javax.servlet` (igual
   que el proyecto original), por eso se recomienda Tomcat **9** o 8.5.
   En Tomcat 10 habría que cambiar los imports a `jakarta.servlet`.
3. Clic derecho en el proyecto → **Run**. Maven descargará el conector MySQL
   y se abrirá <http://localhost:8080/SeguridadDigital/>.

Si prefieres terminal (con Maven instalado):

```bash
cd SeguridadDigital
mvn package
# copia target/SeguridadDigital.war a tomcat/webapps/
```

### Opción B: NetBeans estilo clásico (como el ZIP original)

1. Crea un proyecto nuevo **Java Web → Web Application** y asígnale un Tomcat.
2. Copia el contenido de `src/java` dentro de la carpeta `Source Packages`
   del proyecto nuevo.
3. Copia el contenido de `web` dentro de la carpeta `Web Pages` del proyecto.
4. Descarga `mysql-connector-j-8.4.0.jar` y ponlo en
   `Web Pages/WEB-INF/lib/` (o agrégalo como librería del proyecto).
5. Ejecuta el proyecto.

### Si no compila el conector

El proyecto usa el driver nuevo (`com.mysql.cj.jdbc.Driver`). Si usas un JAR
antiguo del conector, `ConexionBD` también intenta `com.mysql.jdbc.Driver`
automáticamente.

---

## 4. Estructura

```
SeguridadDigital/
├── database/seguridad.sql          <-SCRIPT PARA XAMPP
├── pom.xml
├── src/
│   ├── conf/MANIFEST.MF
│   └── java/com/sentinel/
│       ├── util/        ConexionBD, Constantes, Html, AuditoriaUtil
│       ├── model/       Usuario, Rol, Asistencia, Activo, Auditoria, Estadisticas
│       ├── dao/         UsuarioDAO, AsistenciaDAO, ActivoDAO, AuditoriaDAO, EstadisticasDAO
│       ├── filter/      SeguridadFilter (control de sesión y permisos)
│       └── servlet/     Login, Logout, Asistencia, Activo, Usuario, Perfil
└── web/
    ├── index.jsp                  <- pantalla de entrada (dos perfiles)
    ├── login.jsp
    ├── dashboard.jsp
    ├── asistencias.jsp / asistencia-form.jsp
    ├── activos.jsp / activo-form.jsp
    ├── usuarios.jsp / usuario-form.jsp
    ├── auditoria.jsp / perfil.jsp / acceso-denegado.jsp
    ├── inc/                       <- cabecera y menú comunes
    ├── css/estilos.css
    ├── js/app.js
    ├── imagenes/                  <- logotipo, banners, tarjetas e iconos SVG
    └── WEB-INF/web.xml
```

---

## 5. Nota de seguridad (importante para un proyecto real)

Las contraseñas se guardan **en texto plano** en la tabla `usuarios` para que
sean visibles en phpMyAdmin y el Ejercicio sea fácil de revisar, igual que en
el programa original. En un sistema real deben guardarse con `SHA-256` + `salt`
o `bcrypt`, y nunca mostrar `adm123` / `per123` en una pantalla de ayuda.
