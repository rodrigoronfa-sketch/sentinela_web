package com.sentinel.dao;

import com.sentinel.model.Auditoria;
import com.sentinel.model.Usuario;
import com.sentinel.util.Constantes;
import com.sentinel.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {

    /**
     * Registra una accion. Si se recibe una conexion abierta se reutiliza;
     * si no, se abre una nueva.
     */
    public void registrar(Connection cn, Integer idUsuario, String usuario,
                          String accion, String modulo, String detalle, String ip) {
        boolean cerrar = false;
        Connection conexion = cn;
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = ConexionBD.getConexion();
                cerrar = true;
            }
            String sql = "INSERT INTO auditoria "
                    + "(id_usuario, usuario, accion, modulo, detalle, direccion_ip) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                if (idUsuario == null || idUsuario <= 0) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, idUsuario);
                }
                ps.setString(2, usuario);
                ps.setString(3, accion);
                ps.setString(4, modulo);
                ps.setString(5, detalle);
                ps.setString(6, ip);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cerrar && conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ignorado) {
                    /* nada que hacer */
                }
            }
        }
    }

    /** El administrador ve todo; el personal unicamente sus propios eventos. */
    public List<Auditoria> listar(Usuario usuario) {
        List<Auditoria> lista = new ArrayList<Auditoria>();
        StringBuilder sql = new StringBuilder("SELECT a.* FROM auditoria a WHERE 1 = 1 ");
        List<Object> parametros = new ArrayList<Object>();

        if (usuario != null && !Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            sql.append("AND a.id_usuario = ? ");
            parametros.add(usuario.getIdUsuario());
        }
        sql.append("ORDER BY a.fecha_evento DESC LIMIT 300");

        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Auditoria a = new Auditoria();
                    a.setIdAuditoria(rs.getInt("id_auditoria"));
                    a.setIdUsuario(rs.getInt("id_usuario"));
                    a.setUsuario(rs.getString("usuario"));
                    a.setAccion(rs.getString("accion"));
                    a.setModulo(rs.getString("modulo"));
                    a.setDetalle(rs.getString("detalle"));
                    a.setIp(rs.getString("direccion_ip"));
                    a.setFechaEvento(rs.getString("fecha_evento"));
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
