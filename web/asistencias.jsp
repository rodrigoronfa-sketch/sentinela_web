<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.AsistenciaDAO, com.sentinel.dao.UsuarioDAO" %>
<%@ page import="com.sentinel.model.Asistencia, com.sentinel.model.Usuario" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    request.setAttribute("tituloPagina", "Asistencias");
    request.setAttribute("moduloActivo", "asistencias");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    String estado = request.getParameter("estado");
    List<Asistencia> lista = new AsistenciaDAO().listar(estado, usuarioSesion);
    boolean admin = Constantes.ROL_ADMIN.equals(usuarioSesion.getNombreRol());
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
%>

<div class="cabecera-pagina">
    <h1>Gestión de asistencias</h1>
    <p><%= admin ? "Registra entrada y salida del personal y revisa su historial."
                 : "Registra tu asistencia y da seguimiento a tu historial de entrada y salida." %></p>
</div>

<% if (ok != null) { %><div class="alerta ok"><%= Html.esc(ok) %></div><% } %>
<% if (error != null) { %><div class="alerta error"><%= Html.esc(error) %></div><% } %>

<section class="card">
    <form method="get" action="<%= ctx %>/asistencias.jsp" class="filtros">
        <div class="campo">
            <label for="estado">Estado</label>
            <select id="estado" name="estado">
                <option value="">Todos los estados</option>
                <option value="Presente" <%= "Presente".equals(estado) ? "selected" : "" %>>Presente</option>
                <option value="Ausente" <%= "Ausente".equals(estado) ? "selected" : "" %>>Ausente</option>
                <option value="Retrasado" <%= "Retrasado".equals(estado) ? "selected" : "" %>>Retrasado</option>
                <option value="Justificada" <%= "Justificada".equals(estado) ? "selected" : "" %>>Justificada</option>
            </select>
        </div>
        <div class="campo" style="flex:2">
            <label for="buscar">Búsqueda rápida</label>
            <input type="text" id="buscar" data-buscar="tabla-asistencias" placeholder="Filtrar la tabla...">
        </div>
        <button class="btn btn-dark" type="submit">Filtrar</button>
        <a class="btn btn-primary" href="<%= ctx %>/asistencia-form.jsp?accion=nuevo">+ Nueva asistencia</a>
    </form>
</section>

<section class="card sinvpad">
    <div class="card-head">
        <h2>Listado de asistencias (<%= lista.size() %>)</h2>
    </div>
    <div class="tabla-wrap">
    <% if (lista.isEmpty()) { %>
        <div class="vacio">
            <img src="<%= ctx %>/imagenes/icon-asistencias.svg" alt="">
            No hay asistencias con estos filtros.
        </div>
    <% } else { %>
        <table class="tabla" id="tabla-asistencias">
            <thead>
                <tr>
                    <th>Código</th><th>Empleado</th><th>Fecha</th>
                    <th>Entrada</th><th>Salida</th><th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% for (Asistencia a : lista) { %>
                <% boolean puedeEditar = admin
                        || a.getIdEmpleado() == usuarioSesion.getIdUsuario(); %>
                <tr>
                    <td><strong><%= Html.esc(a.getCodigo()) %></strong></td>
                    <td><%= Html.esc(a.getEmpleadoTexto()) %></td>
                    <td><span class="secundario"><%= Html.esc(a.getFecha()) %></span></td>
                    <td><%= Html.esc(a.getHoraEntrada()) %></td>
                    <td><%= Html.esc(a.getHoraSalida()) %></td>
                    <td><span class="badge <%= a.getEstadoClase() %>"><%= Html.esc(a.getEstado()) %></span></td>
                    <td>
                        <div class="acciones">
                        <% if (puedeEditar) { %>
                            <a class="btn btn-sm btn-outline" href="<%= ctx %>/asistencia-form.jsp?accion=editar&id=<%= a.getIdAsistencia() %>">Editar</a>
                        <% } %>
                        <% if (admin) { %>
                            <form method="post" action="<%= ctx %>/asistencia">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="<%= a.getIdAsistencia() %>">
                                <button class="btn btn-sm btn-danger" type="submit"
                                        data-confirm="¿Eliminar definitivamente la asistencia <%= Html.esc(a.getCodigo()) %>?">Eliminar</button>
                            </form>
                        <% } %>
                        </div>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
    </div>
</section>

<%@ include file="inc/pie.jspf" %>
