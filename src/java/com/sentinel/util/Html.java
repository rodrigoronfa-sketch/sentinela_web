package com.sentinel.util;

/**
 * Escapa texto antes de escribirlo en una pagina JSP.
 */
public final class Html {

    private Html() {
    }

    public static String esc(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    public static String nuloAVacio(String valor) {
        return valor == null ? "" : valor;
    }
}
