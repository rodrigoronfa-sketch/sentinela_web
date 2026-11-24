package com.sentinel.util;

import com.sentinel.dao.AuditoriaDAO;
import com.sentinel.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Helper para registrar acciones en la tabla auditoria desde los servlets.
 */
public final class AuditoriaUtil {

    private AuditoriaUtil() {
    }

    public static void registrar(HttpServletRequest request, String accion,
                                 String modulo, String detalle) {
        HttpSession sesion = request.getSession(false);
        Usuario usuario = null;
        if (sesion != null) {
            usuario = (Usuario) sesion.getAttribute(Constantes.ATTR_USUARIO);
        }

        Integer idUsuario = usuario == null ? null : usuario.getIdUsuario();
        String nombreUsuario = usuario == null ? "anonimo" : usuario.getUsuario();
        String ip = request.getRemoteAddr();

        new AuditoriaDAO().registrar(null, idUsuario, nombreUsuario,
                                     accion, modulo, detalle, ip);
    }
}
