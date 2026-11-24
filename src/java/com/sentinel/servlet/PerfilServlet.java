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
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PerfilServlet", urlPatterns = { "/perfil" })
public class PerfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute(Constantes.ATTR_USUARIO);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        String actual = valor(request.getParameter("clave_actual"));
        String nueva = valor(request.getParameter("clave_nueva"));
        String confirmar = valor(request.getParameter("clave_confirmar"));

        boolean cambiarClave = !nueva.isEmpty();
        if (cambiarClave) {
            String guardada = dao.obtenerPassword(usuario.getIdUsuario());
            if (guardada == null || !guardada.equals(actual)) {
                redirigir(request, response, "error", "La contraseña actual no es correcta.");
                return;
            }
            if (!nueva.equals(confirmar)) {
                redirigir(request, response, "error", "La nueva contraseña y la confirmación no coinciden.");
                return;
            }
        }

        usuario.setNombres(valor(request.getParameter("nombres")));
        usuario.setApellidos(valor(request.getParameter("apellidos")));
        usuario.setEmail(valor(request.getParameter("email")));

        boolean ok = dao.actualizar(usuario);
        if (ok && cambiarClave) {
            ok = dao.cambiarPassword(usuario.getIdUsuario(), nueva);
        }
        if (ok) {
            AuditoriaUtil.registrar(request,
                    cambiarClave ? "Actualizar perfil y contraseña" : "Actualizar perfil",
                    "Configuracion", usuario.getUsuario());
            sesion.setAttribute(Constantes.ATTR_USUARIO, usuario);
        }

        redirigir(request, response, ok ? "ok" : "error",
                  ok ? "Perfil actualizado correctamente." : "No se pudo actualizar el perfil.");
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response,
                           String tipo, String mensaje) throws IOException {
        response.sendRedirect(request.getContextPath() + "/perfil.jsp?"
                + tipo + "=" + URLEncoder.encode(mensaje, "UTF-8"));
    }

    private String valor(String s) {
        return s == null ? "" : s.trim();
    }
}
