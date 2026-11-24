package com.sentinel.servlet;

import com.sentinel.dao.UsuarioDAO;
import com.sentinel.model.Usuario;
import com.sentinel.util.AuditoriaUtil;
import com.sentinel.util.Constantes;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = valor(request.getParameter("usuario"));
        String clave = valor(request.getParameter("clave"));
        String rol = valor(request.getParameter("rol"));

        UsuarioDAO dao = new UsuarioDAO();
        Usuario encontrado = (usuario == null || clave == null)
                ? null : dao.autenticar(usuario, clave);

        if (encontrado == null || rol == null || !rol.equals(encontrado.getNombreRol())) {
            request.setAttribute("error", "Credenciales incorrectas o el tipo de usuario no coincide.");
            request.setAttribute("usuario", usuario == null ? "" : usuario);
            request.setAttribute("rol", rol == null ? "" : rol);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession sesion = request.getSession();
        sesion.setAttribute(Constantes.ATTR_USUARIO, encontrado);
        dao.actualizarUltimoAcceso(encontrado.getIdUsuario());
        AuditoriaUtil.registrar(request, "Inicio de sesion", "Seguridad",
                                "Acceso del usuario " + encontrado.getUsuario());

        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
    }

    private String valor(String s) {
        return s == null ? "" : s.trim();
    }
}
