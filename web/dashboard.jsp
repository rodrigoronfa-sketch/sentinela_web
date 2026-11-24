<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.EstadisticasDAO, com.sentinel.dao.AsistenciaDAO, com.sentinel.dao.AuditoriaDAO" %>
<%@ page import="com.sentinel.model.Estadisticas, com.sentinel.model.Asistencia, com.sentinel.model.Auditoria" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    request.setAttribute("tituloPagina", "Panel de control");
    request.setAttribute("moduloActivo", "dashboard");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    Estadisticas stats = new EstadisticasDAO().obtenerPara(usuarioSesion);
    List<Asistencia> recientes = new AsistenciaDAO().listar(null, usuarioSesion);
    List<Auditoria> eventos = new AuditoriaDAO().listar(usuarioSesion);
    boolean admin = Constantes.ROL_ADMIN.equals(usuarioSesion.getNombreRol());
%>

<div class="bienvenida">
    <h1>Hola, <%= Html.esc(usuarioSesion.getNombres()) %>
        <span class="badge-rol <%= admin ? "admin" : "" %>">
            <%= admin ? "ADMINISTRADOR" : "PERSONAL DE SEGURIDAD" %>
        </span>
    </h1>
    <p>Este es el centro de operaciones de SENTINELA. Aquí puedes registrar y controlar las
       asistencias del personal, revisar los activos vigilados y consultar la actividad registrada.</p>
</div>

<div class="grid grid-4">
    <div class="stat cyan">
        <div class="etiqueta">Asistencias registradas</div>
        <div class="numero"><%= stats.getTotalAsistencias() %></div>
        <div class="nota"><%= admin ? "Registradas en el sistema" : "Relacionadas contigo" %></div>
    </div>
    <div class="stat azul">
        <div class="etiqueta">Presentes</div>
        <div class="numero"><%= stats.getPresentes() %></div>
        <div class="nota">Con registro de entrada</div>
    </div>
    <div class="stat rojo">
        <div class="etiqueta">Faltas</div>
        <div class="numero"><%= stats.getAusentes() %></div>
        <div class="nota">Sin registro de asistencia</div>
    </div>
    <div class="stat verde">
        <div class="etiqueta">Retrasados</div>
        <div class="numero"><%= stats.getRetrasados() %></div>
        <div class="nota">Llegadas tarde</div>
    </div>
</div>

<div class="grid <%= admin ? "grid-3" : "grid-2" %>">
    <a class="accion-rapida" href="<%= ctx %>/asistencia-form.jsp?accion=nuevo">
        <img src="<%= ctx %>/imagenes/card-asistencias.svg" alt="">
        <div><b>Registrar asistencia</b><small>Registra la entrada o salida de un empleado.</small></div>
    </a>
    <a class="accion-rapida" href="<%= ctx %>/activos.jsp">
        <img src="<%= ctx %>/imagenes/card-activos.svg" alt="">
        <div><b>Ver activos</b><small>Consulta servidores, equipos y aplicaciones monitorizados.</small></div>
    </a>
    <% if (admin) { %>
    <a class="accion-rapida" href="<%= ctx %>/usuarios.jsp">
        <img src="<%= ctx %>/imagenes/card-usuarios.svg" alt="">
        <div><b>Gestionar usuarios</b><small>Alta, edición y permisos del personal del sistema.</small></div>
    </a>
    <% } %>
</div>

<div class="grid grid-2">
    <section class="card sinvpad">
        <div class="card-head">
            <h2>Últimas asistencias</h2>
            <a href="<%= ctx %>/asistencias.jsp" class="btn btn-sm btn-outline">Ver todas</a>
        </div>
        <div class="tabla-wrap">
            <% if (recientes.isEmpty()) { %>
                <div class="vacio">No hay asistencias registradas.</div>
            <% } else { %>
            <table class="tabla" id="tabla-recientes">
                <thead>
                    <tr><th>Código</th><th>Empleado</th><th>Estado</th></tr>
                </thead>
                <tbody>
                <% int contador = 0; %>
                <% for (Asistencia a : recientes) { %>
                    <% if (contador++ >= 5) break; %>
                    <tr>
                        <td><strong><%= Html.esc(a.getCodigo()) %></strong></td>
                        <td><%= Html.esc(a.getEmpleadoTexto()) %></td>
                        <td><span class="badge <%= a.getEstadoClase() %>"><%= Html.esc(a.getEstado()) %></span></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>
        </div>
    </section>

    <section class="card">
        <h2>Actividad reciente</h2>
        <p><small>Registro de auditoría del sistema</small></p>
        <ul class="timeline">
        <% int n = 0; %>
        <% for (Auditoria a : eventos) { %>
            <% if (n++ >= 6) break; %>
            <li>
                <span class="punto"></span>
                <div>
                    <b><%= Html.esc(a.getAccion()) %></b>
                    <%= Html.esc(a.getModulo()) %> &middot; <%= Html.esc(a.getUsuario()) %>
                    <small><%= Html.esc(a.getFechaEvento()) %></small>
                </div>
            </li>
        <% } %>
        <% if (n == 0) { %>
            <li><span class="punto"></span><div>Sin actividad registrada.</div></li>
        <% } %>
        </ul>
        <a href="<%= ctx %>/auditoria.jsp" class="btn btn-sm btn-outline">Ver auditoría completa</a>
    </section>
</div>

<%@ include file="inc/pie.jspf" %>
