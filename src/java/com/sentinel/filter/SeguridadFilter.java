package com.sentinel.filter;

import com.sentinel.model.Usuario;
import com.sentinel.util.Constantes;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * protege las paginas del sistema y separa los permisos por rol.
 */
@WebFilter(filterName = "SeguridadFilter", urlPatterns = { "/*" })
public class SeguridadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String ruta = request.getRequestURI();
        String contexto = request.getContextPath();
        if (ruta.startsWith(contexto)) {
            ruta = ruta.substring(contexto.length());
        }

        if (esPublica(ruta)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sesion = request.getSession(false);
        Usuario usuario = null;
        if (sesion != null) {
            usuario = (Usuario) sesion.getAttribute(Constantes.ATTR_USUARIO);
        }

        if (usuario == null) {
            response.sendRedirect(contexto + "/login.jsp");
            return;
        }

        /* La gestion de usuarios es exclusiva del administrador */
        if ((ruta.startsWith("/usuarios") || ruta.startsWith("/usuario"))
                && !Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            response.sendRedirect(contexto + "/acceso-denegado.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean esPublica(String ruta) {
            return ruta.equals("/")
                    || ruta.equals("/index.jsp")
                    || ruta.startsWith("/login")
                    || ruta.startsWith("/logout")
                    || ruta.equals("/acceso-denegado.jsp")
                    || ruta.startsWith("/css/")
                    || ruta.startsWith("/js/")
                    || ruta.startsWith("/imagenes/")
                    || ruta.startsWith("/inc/");
        }
    @Override
    public void destroy() {
    }
}
