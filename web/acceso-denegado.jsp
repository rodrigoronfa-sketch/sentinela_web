<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso denegado - SENTINELA</title>
    <link rel="icon" href="<%= ctx %>/imagenes/favicon.svg">
    <link rel="stylesheet" href="<%= ctx %>/css/estilos.css">
</head>
<body>
<div class="portada">
    <div class="portada-card" style="max-width:620px">
        <div class="portada-head">
            <img class="logo" src="<%= ctx %>/imagenes/logo-sentinel.svg" alt="Logo">
            <h1>Acceso <span>denegado</span></h1>
            <p>No tienes permisos para utilizar esta función.
               Si necesitas acceso, solicítalo al administrador del sistema.</p>
        </div>
        <div class="portada-cred">
            <a class="btn btn-primary" href="<%= ctx %>/dashboard.jsp">Volver al panel</a>
            <a class="btn btn-outline" href="<%= ctx %>/logout">Cerrar sesión</a>
        </div>
        <div class="portada-foot">SENTINELA · Seguridad Digital</div>
    </div>
</div>
</body>
</html>
