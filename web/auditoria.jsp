<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.AuditoriaDAO" %>
<%@ page import="com.sentinel.model.Auditoria" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    request.setAttribute("tituloPagina", "Auditoría");
    request.setAttribute("moduloActivo", "auditoria");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    List<Auditoria> lista = new AuditoriaDAO().listar(usuarioSesion);
    boolean admin = Constantes.ROL_ADMIN.equals(usuarioSesion.getNombreRol());
%>

<div class="cabecera-pagina">
    <h1>Registro de auditoría</h1>
    <p><%= admin ? "Todas las acciones realizadas en el sistema."
                 : "Historial de acciones realizadas con tu cuenta." %></p>
</div>

<section class="card">
    <div class="alerta info">
        <strong><%= admin ? "Vista de administrador" : "Vista personal" %></strong> &mdash;
        <%= admin ? "Se muestran los eventos de todos los usuarios (máximo 300)."
                  : "Solo se muestran tus propios eventos (máximo 300)." %>
    </div>
    <div class="campo" style="max-width:360px">
        <label for="buscar">Búsqueda rápida</label>
        <input type="text" id="buscar" data-buscar="tabla-auditoria" placeholder="Filtrar por acción, usuario, módulo...">
    </div>
</section>

<section class="card sinvpad">
    <div class="card-head"><h2>Eventos registrados (<%= lista.size() %>)</h2></div>
    <div class="tabla-wrap">
    <% if (lista.isEmpty()) { %>
        <div class="vacio">
            <img src="<%= ctx %>/imagenes/icon-auditoria.svg" alt="">
            No hay eventos registrados.
        </div>
    <% } else { %>
        <table class="tabla" id="tabla-auditoria">
            <thead>
                <tr><th>Fecha</th><th>Usuario</th><th>Acción</th><th>Módulo</th><th>Detalle</th><th>IP</th></tr>
            </thead>
            <tbody>
            <% for (Auditoria a : lista) { %>
                <tr>
                    <td><%= Html.esc(a.getFechaEvento()) %></td>
                    <td><strong><%= Html.esc(a.getUsuario()) %></strong></td>
                    <td><span class="badge info"><%= Html.esc(a.getAccion()) %></span></td>
                    <td><%= Html.esc(a.getModulo()) %></td>
                    <td><%= Html.esc(a.getDetalle()) %></td>
                    <td><%= Html.esc(a.getIp()) %></td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
    </div>
</section>

<%@ include file="inc/pie.jspf" %>
