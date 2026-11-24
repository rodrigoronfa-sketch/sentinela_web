package com.sentinel.servlet;

import com.sentinel.dao.ActivoDAO;
import com.sentinel.model.Activo;
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

@WebServlet(name = "ActivoServlet", urlPatterns = { "/activo" })
public class ActivoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.ATTR_USUARIO);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }

        String accion = valor(request.getParameter("accion"));
        ActivoDAO dao = new ActivoDAO();
        boolean ok = false;
        String mensaje = "Accion no reconocida.";

        if ("crear".equals(accion) || "actualizar".equals(accion)) {
            Activo a = new Activo();
            if ("actualizar".equals(accion)) {
                a.setIdActivo(entero(request.getParameter("id")));
            }
            a.setNombre(valor(request.getParameter("nombre")));
            a.setTipo(valor(request.getParameter("tipo")));
            a.setIp(valor(request.getParameter("ip")));
            a.setUbicacion(valor(request.getParameter("ubicacion")));
            a.setResponsable(valor(request.getParameter("responsable")));
            a.setNivelCriticidad(valor(request.getParameter("nivel_criticidad")));
            a.setEstado(valor(request.getParameter("estado")));

            if ("crear".equals(accion)) {
                ok = dao.crear(a);
                mensaje = ok ? "Activo registrado correctamente." : "No se pudo registrar el activo.";
            } else {
                ok = dao.actualizar(a);
                mensaje = ok ? "Activo actualizado correctamente." : "No se pudo actualizar el activo.";
            }
            if (ok) {
                AuditoriaUtil.registrar(request,
                        "crear".equals(accion) ? "Crear activo" : "Actualizar activo",
                        "Activos", a.getNombre());
            }
        } else if ("eliminar".equals(accion)) {
            int id = entero(request.getParameter("id"));
            ok = dao.eliminar(id);
            mensaje = ok ? "Activo eliminado." : "No se pudo eliminar el activo.";
            if (ok) {
                AuditoriaUtil.registrar(request, "Eliminar activo", "Activos", "ID " + id);
            }
        }

        response.sendRedirect(request.getContextPath() + "/activos.jsp?"
                + (ok ? "ok" : "error") + "=" + URLEncoder.encode(mensaje, "UTF-8"));
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
}
