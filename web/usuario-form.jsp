<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.UsuarioDAO" %>
<%@ page import="com.sentinel.model.Usuario" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes" %>
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
    String accion = request.getParameter("accion");
    if (accion == null || accion.isEmpty()) {
        accion = "nuevo";
    }
    boolean editar = "editar".equals(accion);
    Usuario u = new Usuario();
    if (editar) {
        int id = 0;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (Exception e) { id = 0; }
        Usuario encontrado = new UsuarioDAO().obtener(id);
        if (encontrado != null) {
            u = encontrado;
        }
    }
%>

<div class="cabecera-pagina">
    <h1><%= editar ? "Editar usuario" : "Nuevo usuario" %></h1>
    <p><%= editar ? "Modifica los datos y el rol del usuario." : "Crea una cuenta para el personal de seguridad." %></p>
</div>

<section class="card">
    <form method="post" action="<%= ctx %>/usuario">
        <input type="hidden" name="accion" value="<%= editar ? "actualizar" : "crear" %>">
        <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
        <input type="hidden" name="activo" value="<%= u.isActivo() ? "1" : "0" %>">

        <div class="form-grid">
            <% if (!editar) { %>
            <div class="campo">
                <label for="usuario">Usuario <span class="requerido">*</span></label>
                <input type="text" id="usuario" name="usuario" required maxlength="30"
                       placeholder="Ej. carla" autocomplete="off">
            </div>
            <div class="campo">
                <label for="password">Contraseña <span class="requerido">*</span></label>
                <input type="text" id="password" name="password" required maxlength="100"
                       placeholder="Ej. carla123" autocomplete="off">
            </div>
            <% } %>

            <div class="campo">
                <label for="nombres">Nombres <span class="requerido">*</span></label>
                <input type="text" id="nombres" name="nombres" required value="<%= Html.esc(u.getNombres()) %>">
            </div>
            <div class="campo">
                <label for="apellidos">Apellidos <span class="requerido">*</span></label>
                <input type="text" id="apellidos" name="apellidos" required value="<%= Html.esc(u.getApellidos()) %>">
            </div>
            <div class="campo">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" value="<%= Html.esc(u.getEmail()) %>" placeholder="nombre@sentinelaseguridad.com">
            </div>
            <div class="campo">
                <label for="id_rol">Rol</label>
                <select id="id_rol" name="id_rol">
                    <option value="2" <%= u.getIdRol() == 2 || u.getIdRol() == 0 ? "selected" : "" %>>PERSONAL</option>
                    <option value="1" <%= u.getIdRol() == 1 ? "selected" : "" %>>ADMIN</option>
                </select>
            </div>
            <div class="campo full">
                <label for="foto">Ruta de la foto (SVG)</label>
                <input type="text" id="foto" name="foto" value="<%= Html.esc(u.getFoto()) %>"
                       placeholder="imagenes/sin-foto.svg">
            </div>
        </div>

        <div class="form-acciones">
            <button class="btn btn-primary" type="submit"><%= editar ? "Guardar cambios" : "Crear usuario" %></button>
            <a class="btn btn-outline" href="<%= ctx %>/usuarios.jsp">Cancelar</a>
        </div>
    </form>
</section>

<%@ include file="inc/pie.jspf" %>
