<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.ActivoDAO" %>
<%@ page import="com.sentinel.model.Activo" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes" %>
<%
    com.sentinel.model.Usuario sesionUsuario =
            (com.sentinel.model.Usuario) session.getAttribute(Constantes.ATTR_USUARIO);
    if (sesionUsuario == null || !Constantes.ROL_ADMIN.equals(sesionUsuario.getNombreRol())) {
        response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
        return;
    }
    request.setAttribute("tituloPagina", "Activos vigilados");
    request.setAttribute("moduloActivo", "activos");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    boolean admin = true;
    String accion = request.getParameter("accion");
    if (accion == null || accion.isEmpty()) {
        accion = "nuevo";
    }
    boolean editar = "editar".equals(accion);
    Activo a = new Activo();
    if (editar) {
        int id = 0;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (Exception e) { id = 0; }
        Activo encontrado = new ActivoDAO().obtener(id);
        if (encontrado != null) {
            a = encontrado;
        }
    }
%>

<div class="cabecera-pagina">
    <h1><%= editar ? "Editar activo" : "Nuevo activo" %></h1>
    <p>Información del activo tecnológico vigilado por SENTINELA.</p>
</div>

<section class="card">
    <form method="post" action="<%= ctx %>/activo">
        <input type="hidden" name="accion" value="<%= editar ? "actualizar" : "crear" %>">
        <input type="hidden" name="id" value="<%= a.getIdActivo() %>">

        <div class="form-grid">
            <div class="campo full">
                <label for="nombre">Nombre del activo <span class="requerido">*</span></label>
                <input type="text" id="nombre" name="nombre" required maxlength="120"
                       value="<%= Html.esc(a.getNombre()) %>" placeholder="Ej. Servidor principal de producción">
            </div>

            <div class="campo">
                <label for="tipo">Tipo</label>
                <select id="tipo" name="tipo">
                    <% String[] tipos = {"Servidor", "Computadora", "Switch/Router", "Aplicación",
                                         "Cuenta de usuario", "Base de datos", "Otro"}; %>
                    <% for (String t : tipos) { %>
                        <option value="<%= t %>" <%= t.equals(a.getTipo()) ? "selected" : "" %>><%= t %></option>
                    <% } %>
                </select>
            </div>

            <div class="campo">
                <label for="ip">Dirección IP</label>
                <input type="text" id="ip" name="ip" value="<%= Html.esc(a.getIp()) %>" placeholder="192.168.10.20">
            </div>

            <div class="campo">
                <label for="ubicacion">Ubicación</label>
                <input type="text" id="ubicacion" name="ubicacion" value="<%= Html.esc(a.getUbicacion()) %>" placeholder="Sala de servidores">
            </div>

            <div class="campo">
                <label for="responsable">Responsable</label>
                <input type="text" id="responsable" name="responsable" value="<%= Html.esc(a.getResponsable()) %>" placeholder="Área o persona a cargo">
            </div>

            <div class="campo">
                <label for="nivel_criticidad">Nivel de criticidad</label>
                <select id="nivel_criticidad" name="nivel_criticidad">
                    <% String[] crit = {"Bajo", "Medio", "Alto", "Crítico"}; %>
                    <% for (String c : crit) { %>
                        <option value="<%= c %>" <%= c.equals(a.getNivelCriticidad()) ? "selected" : "" %>><%= c %></option>
                    <% } %>
                </select>
            </div>

            <div class="campo">
                <label for="estado">Estado</label>
                <select id="estado" name="estado">
                    <% String[] est = {"Operativo", "En mantenimiento", "En riesgo", "Fuera de servicio"}; %>
                    <% for (String e : est) { %>
                        <option value="<%= e %>" <%= e.equals(a.getEstado()) ? "selected" : "" %>><%= e %></option>
                    <% } %>
                </select>
            </div>
        </div>

        <div class="form-acciones">
            <button class="btn btn-primary" type="submit"><%= editar ? "Guardar cambios" : "Registrar activo" %></button>
            <a class="btn btn-outline" href="<%= ctx %>/activos.jsp">Cancelar</a>
        </div>
    </form>
</section>

<%@ include file="inc/pie.jspf" %>
