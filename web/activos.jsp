<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.ActivoDAO" %>
<%@ page import="com.sentinel.model.Activo" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    request.setAttribute("tituloPagina", "Activos vigilados");
    request.setAttribute("moduloActivo", "activos");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    String estado = request.getParameter("estado");
    String criticidad = request.getParameter("criticidad");
    List<Activo> lista = new ActivoDAO().listar(estado, criticidad);
    boolean admin = Constantes.ROL_ADMIN.equals(usuarioSesion.getNombreRol());
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
%>

<div class="cabecera-pagina">
    <h1>Activos vigilados</h1>
    <p><%= admin ? "Administra el inventario tecnológico y su nivel de criticidad."
                 : "Consulta los equipos, sistemas y cuentas monitorizados por SENTINELA." %></p>
</div>

<% if (ok != null) { %><div class="alerta ok"><%= Html.esc(ok) %></div><% } %>
<% if (error != null) { %><div class="alerta error"><%= Html.esc(error) %></div><% } %>

<section class="card">
    <form method="get" action="<%= ctx %>/activos.jsp" class="filtros">
        <div class="campo">
            <label for="estado">Estado</label>
            <select id="estado" name="estado">
                <option value="">Todos</option>
                <option value="Operativo" <%= "Operativo".equals(estado) ? "selected" : "" %>>Operativo</option>
                <option value="En mantenimiento" <%= "En mantenimiento".equals(estado) ? "selected" : "" %>>En mantenimiento</option>
                <option value="En riesgo" <%= "En riesgo".equals(estado) ? "selected" : "" %>>En riesgo</option>
                <option value="Fuera de servicio" <%= "Fuera de servicio".equals(estado) ? "selected" : "" %>>Fuera de servicio</option>
            </select>
        </div>
        <div class="campo">
            <label for="criticidad">Criticidad</label>
            <select id="criticidad" name="criticidad">
                <option value="">Todas</option>
                <option value="Crítico" <%= "Crítico".equals(criticidad) ? "selected" : "" %>>Crítico</option>
                <option value="Alto" <%= "Alto".equals(criticidad) ? "selected" : "" %>>Alto</option>
                <option value="Medio" <%= "Medio".equals(criticidad) ? "selected" : "" %>>Medio</option>
                <option value="Bajo" <%= "Bajo".equals(criticidad) ? "selected" : "" %>>Bajo</option>
            </select>
        </div>
        <div class="campo" style="flex:2">
            <label for="buscar">Búsqueda rápida</label>
            <input type="text" id="buscar" data-buscar="tabla-activos" placeholder="Filtrar la tabla...">
        </div>
        <button class="btn btn-dark" type="submit">Filtrar</button>
        <% if (admin) { %>
            <a class="btn btn-primary" href="<%= ctx %>/activo-form.jsp?accion=nuevo">+ Nuevo activo</a>
        <% } %>
    </form>
</section>

<section class="card sinvpad">
    <div class="card-head"><h2>Inventario (<%= lista.size() %>)</h2></div>
    <div class="tabla-wrap">
    <% if (lista.isEmpty()) { %>
        <div class="vacio">
            <img src="<%= ctx %>/imagenes/icon-activos.svg" alt="">
            No hay activos con estos filtros.
        </div>
    <% } else { %>
        <table class="tabla" id="tabla-activos">
            <thead>
                <tr>
                    <th>Código</th><th>Activo</th><th>Tipo</th><th>Dirección IP</th>
                    <th>Ubicación</th><th>Criticidad</th><th>Estado</th>
                    <% if (admin) { %><th>Acciones</th><% } %>
                </tr>
            </thead>
            <tbody>
            <% for (Activo a : lista) { %>
                <tr>
                    <td><strong><%= Html.esc(a.getCodigo()) %></strong></td>
                    <td>
                        <span class="principal"><%= Html.esc(a.getNombre()) %></span>
                        <span class="secundario"><%= Html.esc(a.getResponsable()) %></span>
                    </td>
                    <td><%= Html.esc(a.getTipo()) %></td>
                    <td><%= Html.esc(a.getIp()) %></td>
                    <td><%= Html.esc(a.getUbicacion()) %></td>
                    <td><span class="badge <%= a.getCriticidadClase() %>"><%= Html.esc(a.getNivelCriticidad()) %></span></td>
                    <td><span class="badge <%= a.getEstadoClase() %>"><%= Html.esc(a.getEstado()) %></span></td>
                    <% if (admin) { %>
                    <td>
                        <div class="acciones">
                            <a class="btn btn-sm btn-outline" href="<%= ctx %>/activo-form.jsp?accion=editar&id=<%= a.getIdActivo() %>">Editar</a>
                            <form method="post" action="<%= ctx %>/activo">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="<%= a.getIdActivo() %>">
                                <button class="btn btn-sm btn-danger" type="submit"
                                        data-confirm="¿Eliminar el activo <%= Html.esc(a.getCodigo()) %>?">Eliminar</button>
                            </form>
                        </div>
                    </td>
                    <% } %>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
    </div>
</section>

<%@ include file="inc/pie.jspf" %>
