package com.sentinel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexion JDBC con la base de datos "seguridad" de XAMPP.
 * Si tu MySQL tiene contraseña, cambia USER y PASS.
 */
public final class ConexionBD {

        private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/seguridad"
              + "?useSSL=false"
              + "&allowPublicKeyRetrieval=true"
              + "&serverTimezone=UTC"
              + "&useUnicode=true"
              + "&characterEncoding=UTF-8";

    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "root";

    private static final String PASS = System.getenv("DB_PASS") != null
            ? System.getenv("DB_PASS")
            : "";

    private ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {
        cargarDriver();
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static void cargarDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                /* Compatibilidad con versiones antiguas del conector */
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                throw new SQLException("No se encontro el driver de MySQL. "
                        + "Agrega mysql-connector-j a WEB-INF/lib.", e2);
            }
        }
    }
}
