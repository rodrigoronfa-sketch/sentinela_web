<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.UsuarioDAO" %>
<%@ page import="com.sentinel.model.Usuario" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    com.sentinel.model.Usuario sesionUsuario =
            (com.sentinel.model.Usuario) session.getAttribute(Constantes.ATTR_USUARIO);
    if (sesionUsuario == null || !Constantes.ROL_ADMIN.equals(sesionUsuario.getNombreRol())) {
        response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
        return;
    }
    request.setAttribute("tituloPagina", "Gestión de usuarios");
    request.setAttribute("moduloActivo", "usuarios");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    List<Usuario> lista = new UsuarioDAO().listar();
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
%>

<div class="cabecera-pagina">
    <h1>Gestión de usuarios</h1>
    <p>Alta, edición y control de acceso del personal de SENTINELA.</p>
</div>

<% if (ok != null) { %><div class="alerta ok"><%= Html.esc(ok) %></div><% } %>
<% if (error != null) { %><div class="alerta error"><%= Html.esc(error) %></div><% } %>

<section class="card">
    <div class="grid grid-4">
        <div class="stat cyan"><div class="etiqueta">Usuarios</div><div class="numero"><%= lista.size() %></div></div>
        <div class="stat verde"><div class="etiqueta">Activos</div><div class="numero"><%= new UsuarioDAO().listar().size() %></div></div>
        <div class="stat azul"><div class="etiqueta">Administradores</div><div class="numero">
            <% int admines = 0; for (Usuario u : lista) if (Constantes.ROL_ADMIN.equals(u.getNombreRol())) admines++; %><%= admines %>
        </div></div>
        <div class="stat rojo"><div class="etiqueta">Personal</div><div class="numero"><%= lista.size() - admines %></div></div>
    </div>
</section>

<section class="card sinvpad">
    <div class="card-head">
        <h2>Usuarios del sistema</h2>
        <a class="btn btn-primary btn-sm" href="<%= ctx %>/usuario-form.jsp?accion=nuevo">+ Nuevo usuario</a>
    </div>
    <div class="tabla-wrap">
        <table class="tabla" id="tabla-usuarios">
            <thead>
                <tr><th>Usuario</th><th>Nombre</th><th>Correo</th><th>Rol</th>
                    <th>Estado</th><th>Último acceso</th><th>Acciones</th></tr>
            </thead>
            <tbody>
            <% for (Usuario u : lista) { %>
                <tr>
                    <td>
                        <span class="principal"><%= Html.esc(u.getUsuario()) %></span>
                        <span class="secundario"><%= Html.esc(u.getFechaCreacion()) %></span>
                    </td>
                    <td><%= Html.esc(u.getNombreCompleto()) %></td>
                    <td><%= Html.esc(u.getEmail()) %></td>
                    <td>
                        <span class="badge <%= Constantes.ROL_ADMIN.equals(u.getNombreRol()) ? "info" : "gris" %>">
                            <%= Html.esc(u.getNombreRol()) %>
                        </span>
                    </td>
                    <td>
                        <span class="badge <%= u.isActivo() ? "ok" : "danger" %>">
                            <%= u.isActivo() ? "Activo" : "Inactivo" %>
                        </span>
                    </td>
                    <td><%= Html.esc(u.getUltimoAcceso().isEmpty() ? "Nunca" : u.getUltimoAcceso()) %></td>
                    <td>
                        <div class="acciones">
                            <a class="btn btn-sm btn-outline" href="<%= ctx %>/usuario-form.jsp?accion=editar&id=<%= u.getIdUsuario() %>">Editar</a>
                            <form method="post" action="<%= ctx %>/usuario">
                                <input type="hidden" name="accion" value="<%= u.isActivo() ? "desactivar" : "activar" %>">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <button class="btn btn-sm <%= u.isActivo() ? "btn-outline" : "btn-primary" %>" type="submit">
                                    <%= u.isActivo() ? "Desactivar" : "Activar" %>
                                </button>
                            </form>
                            <form method="post" action="<%= ctx %>/usuario">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <button class="btn btn-sm btn-danger" type="submit"
                                        data-confirm="¿Eliminar al usuario <%= Html.esc(u.getUsuario()) %>?">Eliminar</button>
                            </form>
                        </div>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</section>

<%@ include file="inc/pie.jspf" %>
