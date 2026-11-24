<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SENTINELA · Seguridad Digital</title>
    <link rel="icon" href="<%= ctx %>/imagenes/favicon.svg">
    <link rel="stylesheet" href="<%= ctx %>/css/estilos.css">
</head>
<body>
<div class="portada">
    <div class="portada-card">

        <div class="portada-head">
            <img class="logo" src="<%= ctx %>/imagenes/logo-sentinel.svg" alt="Logo SENTINELA">
            <div class="marca">PROTECCIÓN · DETECCIÓN · RESPUESTA</div>
            <h1>SENTINELA <span>Seguridad Digital</span></h1>
            <p>Plataforma interna para el control de asistencias.</p>
        </div>

        <div class="portada-opciones">
            <a class="opcion" href="<%= ctx %>/login.jsp?rol=ADMIN">
                <img src="<%= ctx %>/imagenes/icon-admin.svg" alt="Administrador">
                <div>
                    <strong>Administrador</strong>
                    <small>Gestión completa: usuarios, activos, asistencias y auditoría.</small>
                </div>
                <span class="flecha">&rarr;</span>
            </a>
            <a class="opcion" href="<%= ctx %>/login.jsp?rol=PERSONAL">
                <img src="<%= ctx %>/imagenes/icon-personal.svg" alt="Personal">
                <div>
                    <strong>Personal de seguridad</strong>
                    <small>Registro y seguimiento de asistencias y activos vigilados.</small>
                </div>
                <span class="flecha">&rarr;</span>
            </a>
        </div>

        <div class="portada-foot">
            SENTINELA Seguridad Digital &copy; 2026 &nbsp;·&nbsp; Uso interno autorizado
        </div>
    </div>
</div>
</body>
</html>
