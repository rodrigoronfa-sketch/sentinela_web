package com.sentinel.servlet;

import com.sentinel.dao.UsuarioDAO;
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

@WebServlet(name = "UsuarioServlet", urlPatterns = { "/usuario" })
public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.ATTR_USUARIO);
        if (usuario == null || !Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }

        String accion = valor(request.getParameter("accion"));
        UsuarioDAO dao = new UsuarioDAO();
        boolean ok = false;
        String mensaje = "Accion no reconocida.";

        if ("crear".equals(accion)) {
            Usuario nuevo = new Usuario();
            nuevo.setUsuario(valor(request.getParameter("usuario")));
            nuevo.setPassword(valor(request.getParameter("password")));
            nuevo.setNombres(valor(request.getParameter("nombres")));
            nuevo.setApellidos(valor(request.getParameter("apellidos")));
            nuevo.setEmail(valor(request.getParameter("email")));
            nuevo.setIdRol(entero(request.getParameter("id_rol")));
            nuevo.setActivo(true);
            ok = dao.crear(nuevo);
            mensaje = ok ? "Usuario creado correctamente." : "No se pudo crear el usuario (¿nombre repetido?).";
            if (ok) {
                AuditoriaUtil.registrar(request, "Crear usuario", "Usuarios", nuevo.getUsuario());
            }

        } else if ("actualizar".equals(accion)) {
            Usuario editado = new Usuario();
            editado.setIdUsuario(entero(request.getParameter("id")));
            editado.setNombres(valor(request.getParameter("nombres")));
            editado.setApellidos(valor(request.getParameter("apellidos")));
            editado.setEmail(valor(request.getParameter("email")));
            editado.setIdRol(entero(request.getParameter("id_rol")));
            editado.setFoto(valor(request.getParameter("foto")));
            editado.setActivo(!"0".equals(request.getParameter("activo")));
            ok = dao.actualizar(editado);
            mensaje = ok ? "Usuario actualizado correctamente." : "No se pudo actualizar el usuario.";
            if (ok) {
                AuditoriaUtil.registrar(request, "Actualizar usuario", "Usuarios", "ID " + editado.getIdUsuario());
            }

        } else if ("activar".equals(accion) || "desactivar".equals(accion)) {
            int id = entero(request.getParameter("id"));
            boolean activar = "activar".equals(accion);
            ok = dao.setActivo(id, activar);
            mensaje = ok ? (activar ? "Usuario activado." : "Usuario desactivado.")
                         : "No se pudo cambiar el estado del usuario.";
            if (ok) {
                AuditoriaUtil.registrar(request, activar ? "Activar usuario" : "Desactivar usuario",
                                        "Usuarios", "ID " + id);
            }

        } else if ("eliminar".equals(accion)) {
            int id = entero(request.getParameter("id"));
            ok = dao.eliminar(id);
            mensaje = ok ? "Usuario eliminado."
                         : "No se puede eliminar: el usuario tiene asistencias o registros. Mejor desactívelo.";
            if (ok) {
                AuditoriaUtil.registrar(request, "Eliminar usuario", "Usuarios", "ID " + id);
            }
        }

        response.sendRedirect(request.getContextPath() + "/usuarios.jsp?"
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
