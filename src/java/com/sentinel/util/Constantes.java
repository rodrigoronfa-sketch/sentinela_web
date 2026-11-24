package com.sentinel.util;

/**
 * Constantes compartidas por el sistema SENTINELA.
 */
public final class Constantes {

    private Constantes() {
    }

    public static final String EMPRESA = "SENTINELA";
    public static final String SISTEMA = "Seguridad Digital";

    /** Valores guardados en la tabla roles */
    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_PERSONAL = "PERSONAL";

    /** Atributo de sesión donde se guarda el usuario autenticado */
    public static final String ATTR_USUARIO = "usuarioSesion";
}
