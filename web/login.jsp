<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes" %>
<%
    String ctx = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String usuario = (String) request.getAttribute("usuario");
    if (usuario == null) {
        usuario = "";
    }
    String rol = (String) request.getAttribute("rol");
    if (rol == null || rol.isEmpty()) {
        rol = request.getParameter("rol");
    }
    if (rol == null || rol.isEmpty()) {
        rol = Constantes.ROL_PERSONAL;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión - SENTINELA</title>
    <link rel="icon" href="<%= ctx %>/imagenes/favicon.svg">
    <link rel="stylesheet" href="<%= ctx %>/css/estilos.css">
    <script src="<%= ctx %>/js/app.js" defer></script>
</head>
<body>
<div class="login-wrap">

    <section class="login-arte">
        <div>
            <img src="<%= ctx %>/imagenes/logo-sentinel.svg" alt="Logo SENTINELA">
        </div>
        <div>
            <h2>Protegemos lo que <span>más importa</span></h2>
            <p>Centro de operaciones de seguridad: control de asistencias, activos
               tecnológicos y auditoría en un solo lugar.</p>
            <ul class="lista">
                <li>Registro y trazabilidad de cada asistencia</li>
                <li>Inventario de activos con nivel de criticidad</li>
                <li>Auditoría de todas las acciones del sistema</li>
            </ul>
        </div>
        <small>&copy; 2026 SENTINELA Seguridad Digital</small>
    </section>

    <section class="login-panel">
        <div class="login-box">
            <h1>Bienvenido</h1>
            <p class="sub">Ingresa tus credenciales para acceder al sistema.</p>

            <% if (error != null) { %>
            <div class="alerta error"><%= Html.esc(error) %></div>
            <% } %>

            <form action="<%= ctx %>/login" method="post">
                <div class="rol-selector">
                    <div class="rol-opcion">
                        <input type="radio" id="rol-admin" name="rol" value="ADMIN"
                               <%= Constantes.ROL_ADMIN.equals(rol) ? "checked" : "" %>>
                        <label for="rol-admin">
                            <img src="<%= ctx %>/imagenes/icon-admin.svg" alt=""> Administrador
                        </label>
                    </div>
                    <div class="rol-opcion">
                        <input type="radio" id="rol-personal" name="rol" value="PERSONAL"
                               <%= Constantes.ROL_PERSONAL.equals(rol) ? "checked" : "" %>>
                        <label for="rol-personal">
                            <img src="<%= ctx %>/imagenes/icon-personal.svg" alt=""> Personal
                        </label>
                    </div>
                </div>

                <div class="campo">
                    <label for="usuario">Usuario</label>
                    <input type="text" id="usuario" name="usuario" required
                           autocomplete="username" placeholder="Tu usuario"
                           value="<%= Html.esc(usuario) %>">
                </div>
                <br>
                <div class="campo">
                    <label for="clave">Contraseña</label>
                    <input type="password" id="clave" name="clave" required
                           autocomplete="current-password" placeholder="Tu contraseña">
                </div>

                <br>
                <button type="submit" class="btn btn-primary btn-block">Iniciar sesión</button>
            </form>

            <p style="margin-top:18px;text-align:center">
                <a href="<%= ctx %>/index.jsp" style="color:var(--cyan-strong);font-weight:600">
                    &larr; Volver al inicio
                </a>
            </p>
        </div>
    </section>
</div>
</body>
</html>
