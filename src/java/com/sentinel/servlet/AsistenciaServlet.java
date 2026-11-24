package com.sentinel.servlet;

import com.sentinel.dao.AsistenciaDAO;
import com.sentinel.model.Asistencia;
import com.sentinel.model.Usuario;
import com.sentinel.util.AuditoriaUtil;
import com.sentinel.util.Constantes;
import java.io.IOException;
import java.net.URLEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AsistenciaServlet", urlPatterns = { "/asistencia" })
public class AsistenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.ATTR_USUARIO);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String accion = valor(request.getParameter("accion"));
        AsistenciaDAO dao = new AsistenciaDAO();

        if ("crear".equals(accion)) {
            Asistencia a = new Asistencia();
            int idEmpleado = Constantes.ROL_ADMIN.equals(usuario.getNombreRol())
                    ? entero(request.getParameter("id_empleado")) : 0;
            a.setIdEmpleado(idEmpleado > 0 ? idEmpleado : usuario.getIdUsuario());
            a.setFecha(valor(request.getParameter("fecha")));
            a.setHoraEntrada(valor(request.getParameter("hora_entrada")));
            a.setHoraSalida(valor(request.getParameter("hora_salida")));
            a.setEstado("Presente");
            a.setObservaciones(valor(request.getParameter("observaciones")));

            boolean ok = dao.crear(a);
            if (ok) {
                AuditoriaUtil.registrar(request, "Crear asistencia", "Asistencias",
                                        a.getCodigo() + " - " + a.getFecha());
            }
            redirigir(request, response, "/asistencias.jsp", ok ? "ok" : "error",
                      ok ? "Asistencia registrada correctamente."
                         : "No se pudo registrar la asistencia.");
            return;
        }

        if ("actualizar".equals(accion)) {
            int id = entero(request.getParameter("id"));
            Asistencia actual = dao.obtener(id);
            if (!puedeEditar(actual, usuario)) {
                response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                return;
            }

            actual.setFecha(valor(request.getParameter("fecha")));
            String estado = valor(request.getParameter("estado"));
            if (!estado.isEmpty()) {
                actual.setEstado(estado);
            }
            actual.setHoraEntrada(valor(request.getParameter("hora_entrada")));
            actual.setHoraSalida(valor(request.getParameter("hora_salida")));
            actual.setObservaciones(valor(request.getParameter("observaciones")));
            if (Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
                int idEmpleado = entero(request.getParameter("id_empleado"));
                actual.setIdEmpleado(idEmpleado > 0 ? idEmpleado : usuario.getIdUsuario());
            }

            boolean ok = dao.actualizar(actual);
            if (ok) {
                AuditoriaUtil.registrar(request, "Actualizar asistencia", "Asistencias",
                                        actual.getCodigo() + " - " + actual.getFecha());
            }
            redirigir(request, response, "/asistencias.jsp", ok ? "ok" : "error",
                      ok ? "Asistencia actualizada correctamente."
                         : "No se pudo actualizar la asistencia.");
            return;
        }

        if ("cambiarEstado".equals(accion)) {
            int id = entero(request.getParameter("id"));
            String estado = valor(request.getParameter("estado"));
            Asistencia actual = dao.obtener(id);
            if (!puedeEditar(actual, usuario)) {
                response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                return;
            }
            boolean ok = dao.cambiarEstado(id, estado);
            if (ok) {
                AuditoriaUtil.registrar(request, "Cambiar estado", "Asistencias",
                                        (actual == null ? "" : actual.getCodigo()) + " -> " + estado);
            }
            redirigir(request, response, "/asistencias.jsp", ok ? "ok" : "error",
                      ok ? "Estado actualizado." : "No se pudo cambiar el estado.");
            return;
        }

        if ("eliminar".equals(accion)) {
            if (!Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
                response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                return;
            }
            int id = entero(request.getParameter("id"));
            boolean ok = dao.eliminar(id);
            if (ok) {
                AuditoriaUtil.registrar(request, "Eliminar asistencia", "Asistencias", "ID " + id);
            }
            redirigir(request, response, "/asistencias.jsp", ok ? "ok" : "error",
                      ok ? "Asistencia eliminada." : "No se pudo eliminar la asistencia.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/asistencias.jsp");
    }

    private boolean puedeEditar(Asistencia a, Usuario usuario) {
        if (a == null) {
            return false;
        }
        if (Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            return true;
        }
        return a.getIdEmpleado() == usuario.getIdUsuario();
    }

    private String valor(String s) {
        return s == null ? "" : s.trim();
    }

    private int entero(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response,
                           String pagina, String tipo, String mensaje) throws IOException {
        response.sendRedirect(request.getContextPath() + pagina + "?"
                + tipo + "=" + URLEncoder.encode(mensaje, "UTF-8"));
    }
}
