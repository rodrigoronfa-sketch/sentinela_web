<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes" %>
<%
    request.setAttribute("tituloPagina", "Mi perfil");
    request.setAttribute("moduloActivo", "perfil");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
%>

<div class="cabecera-pagina">
    <h1>Mi perfil</h1>
    <p>Actualiza tus datos personales y cambia tu contraseña de acceso.</p>
</div>

<% if (ok != null) { %><div class="alerta ok"><%= Html.esc(ok) %></div><% } %>
<% if (error != null) { %><div class="alerta error"><%= Html.esc(error) %></div><% } %>

<div class="grid grid-2">
    <section class="card" style="text-align:center">
        <img src="<%= ctx %>/<%= Html.esc(usuarioSesion.getFoto()) %>" alt="Foto de perfil"
             style="width:110px;height:110px;border-radius:50%;margin:0 auto 16px;border:4px solid var(--cyan)">
        <h2><%= Html.esc(usuarioSesion.getNombreCompleto()) %></h2>
        <p><small><%= Html.esc("@" + usuarioSesion.getUsuario()) %></small></p>
        <span class="badge <%= Constantes.ROL_ADMIN.equals(usuarioSesion.getNombreRol()) ? "info" : "ok" %>">
            <%= Html.esc(usuarioSesion.getNombreRol()) %>
        </span>
        <hr style="border:0;border-top:1px solid var(--border);margin:20px 0">
        <p style="text-align:left"><strong>Correo:</strong> <%= Html.esc(usuarioSesion.getEmail()) %><br>
           <strong>Último acceso:</strong> <%= Html.esc(usuarioSesion.getUltimoAcceso().isEmpty() ? "Nunca" : usuarioSesion.getUltimoAcceso()) %><br>
           <strong>Miembro desde:</strong> <%= Html.esc(usuarioSesion.getFechaCreacion()) %></p>
    </section>

    <section class="card">
        <h2>Editar datos y contraseña</h2>
        <p><small>Deja la contraseña nueva en blanco si solo deseas actualizar tus datos.</small></p>
        <form method="post" action="<%= ctx %>/perfil">
            <div class="form-grid">
                <div class="campo">
                    <label for="nombres">Nombres</label>
                    <input type="text" id="nombres" name="nombres" required value="<%= Html.esc(usuarioSesion.getNombres()) %>">
                </div>
                <div class="campo">
                    <label for="apellidos">Apellidos</label>
                    <input type="text" id="apellidos" name="apellidos" required value="<%= Html.esc(usuarioSesion.getApellidos()) %>">
                </div>
                <div class="campo full">
                    <label for="email">Correo electrónico</label>
                    <input type="email" id="email" name="email" value="<%= Html.esc(usuarioSesion.getEmail()) %>">
                </div>
                <div class="campo full"><hr style="border:0;border-top:1px solid var(--border)"></div>
                <div class="campo">
                    <label for="clave_actual">Contraseña actual</label>
                    <input type="password" id="clave_actual" name="clave_actual" autocomplete="current-password">
                </div>
                <div class="campo">
                    <label for="clave_nueva">Contraseña nueva</label>
                    <input type="password" id="clave_nueva" name="clave_nueva" autocomplete="new-password">
                </div>
                <div class="campo full">
                    <label for="clave_confirmar">Confirmar contraseña nueva</label>
                    <input type="password" id="clave_confirmar" name="clave_confirmar" autocomplete="new-password">
                </div>
            </div>
            <div class="form-acciones">
                <button class="btn btn-primary" type="submit">Guardar cambios</button>
                <a class="btn btn-outline" href="<%= ctx %>/dashboard.jsp">Cancelar</a>
            </div>
        </form>
    </section>
</div>

<%@ include file="inc/pie.jspf" %>
