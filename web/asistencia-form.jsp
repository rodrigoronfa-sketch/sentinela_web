<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sentinel.dao.AsistenciaDAO, com.sentinel.dao.UsuarioDAO" %>
<%@ page import="com.sentinel.model.Asistencia, com.sentinel.model.Usuario" %>
<%@ page import="com.sentinel.util.Html, com.sentinel.util.Constantes, java.util.List" %>
<%
    com.sentinel.model.Usuario sesionAsistencia =
            (com.sentinel.model.Usuario) session.getAttribute(Constantes.ATTR_USUARIO);
    if (sesionAsistencia == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    boolean adminAsistencia = Constantes.ROL_ADMIN.equals(sesionAsistencia.getNombreRol());
    String accion = request.getParameter("accion");
    if (accion == null || accion.isEmpty()) {
        accion = "nuevo";
    }
    boolean editar = "editar".equals(accion);
    Asistencia a = new Asistencia();
    boolean permitido = true;

    if (editar) {
        int id = 0;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (Exception e) { id = 0; }
        a = new AsistenciaDAO().obtener(id);
        if (a == null) {
            permitido = false;
        } else if (!adminAsistencia
                && a.getIdEmpleado() != sesionAsistencia.getIdUsuario()) {
            permitido = false;
        }
    }
    request.setAttribute("tituloPagina", "Asistencias");
    request.setAttribute("moduloActivo", "asistencias");
%>
<%@ include file="inc/cabecera.jspf" %>
<%
    boolean admin = adminAsistencia;
    List<Usuario> usuarios = admin ? new UsuarioDAO().listar() : null;
%>

<div class="cabecera-pagina">
    <h1><%= editar ? "Editar asistencia" : "Nueva asistencia" %></h1>
    <p><%= editar ? "Actualiza la fecha, las horas de entrada y salida o el estado."
                  : "Completa los datos del registro de asistencia." %></p>
</div>

<% if (!permitido) { %>
    <div class="alerta error">La asistencia no existe o no tienes permiso para editarla.
        <a href="<%= ctx %>/asistencias.jsp">Volver al listado</a>
    </div>
<% } else { %>
<section class="card">
    <form method="post" action="<%= ctx %>/asistencia">
        <input type="hidden" name="accion" value="<%= editar ? "actualizar" : "crear" %>">
        <input type="hidden" name="id" value="<%= a.getIdAsistencia() %>">

        <div class="form-grid">
            <div class="campo">
                <label for="fecha">Fecha</label>
                <input type="date" id="fecha" name="fecha" required
                       value="<%= Html.esc(a.getFecha()) %>">
            </div>

            <div class="campo">
                <label for="hora_entrada">Hora de entrada</label>
                <input type="time" id="hora_entrada" name="hora_entrada"
                       value="<%= Html.esc(a.getHoraEntrada()) %>">
            </div>

            <div class="campo">
                <label for="hora_salida">Hora de salida</label>
                <input type="time" id="hora_salida" name="hora_salida"
                       value="<%= Html.esc(a.getHoraSalida()) %>">
            </div>

            <% if (admin) { %>
            <div class="campo">
                <label for="id_empleado">Empleado</label>
                <select id="id_empleado" name="id_empleado">
                    <option value="0">Sin seleccionar</option>
                    <% for (Usuario u : usuarios) { %>
                        <option value="<%= u.getIdUsuario() %>"
                            <%= u.getIdUsuario() == a.getIdEmpleado() ? "selected" : "" %>>
                            <%= Html.esc(u.getNombreCompleto()) %> (<%= Html.esc(u.getUsuario()) %>)
                        </option>
                    <% } %>
                </select>
            </div>
            <% } %>

            <% if (editar) { %>
            <div class="campo">
                <label for="estado">Estado</label>
                <select id="estado" name="estado">
                    <% String[] ests = {"Presente", "Ausente", "Retrasado", "Justificada"}; %>
                    <% for (String e : ests) { %>
                        <option value="<%= e %>" <%= e.equals(a.getEstado()) ? "selected" : "" %>><%= e %></option>
                    <% } %>
                </select>
            </div>
            <% } %>

            <div class="campo full">
                <label for="observaciones">Observaciones</label>
                <textarea id="observaciones" name="observaciones"
                          placeholder="Motivo de una ausencia o notas adicionales."><%= Html.esc(a.getObservaciones()) %></textarea>
            </div>
        </div>

        <div class="form-acciones">
            <button type="submit" class="btn btn-primary">
                <%= editar ? "Guardar cambios" : "Registrar asistencia" %>
            </button>
            <a class="btn btn-outline" href="<%= ctx %>/asistencias.jsp">Cancelar</a>
        </div>
    </form>
</section>
<% } %>

<%@ include file="inc/pie.jspf" %>
