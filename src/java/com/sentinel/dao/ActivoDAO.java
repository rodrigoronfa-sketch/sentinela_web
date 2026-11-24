package com.sentinel.dao;

import com.sentinel.model.Activo;
import com.sentinel.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivoDAO {

    public List<Activo> listar(String estado, String criticidad) {
        List<Activo> lista = new ArrayList<Activo>();
        StringBuilder sql = new StringBuilder("SELECT * FROM activos WHERE 1 = 1 ");
        List<Object> parametros = new ArrayList<Object>();

        if (estado != null && !estado.trim().isEmpty()) {
            sql.append("AND estado = ? ");
            parametros.add(estado);
        }
        if (criticidad != null && !criticidad.trim().isEmpty()) {
            sql.append("AND nivel_criticidad = ? ");
            parametros.add(criticidad);
        }
        sql.append("ORDER BY FIELD(nivel_criticidad, 'Crítico', 'Alto', 'Medio', 'Bajo'), id_activo");

        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Activo> listarRecientes(int limite) {
        List<Activo> lista = new ArrayList<Activo>();
        String sql = "SELECT * FROM activos ORDER BY fecha_registro DESC LIMIT ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Activo obtener(int idActivo) {
        String sql = "SELECT * FROM activos WHERE id_activo = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idActivo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String siguienteCodigo() {
        String sql = "SELECT CONCAT('ACT-', LPAD("
                + "COALESCE(MAX(CAST(SUBSTRING_INDEX(codigo, '-', -1) AS UNSIGNED)), 0) + 1, 4, '0')) "
                + "AS codigo FROM activos";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("codigo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ACT-0001";
    }

    public boolean crear(Activo a) {
        String sql = "INSERT INTO activos "
                + "(codigo, nombre, tipo, ip, ubicacion, responsable, nivel_criticidad, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, siguienteCodigo());
            ps.setString(2, a.getNombre());
            ps.setString(3, a.getTipo());
            ps.setString(4, a.getIp());
            ps.setString(5, a.getUbicacion());
            ps.setString(6, a.getResponsable());
            ps.setString(7, a.getNivelCriticidad());
            ps.setString(8, a.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Activo a) {
        String sql = "UPDATE activos SET nombre = ?, tipo = ?, ip = ?, ubicacion = ?, "
                + "responsable = ?, nivel_criticidad = ?, estado = ? WHERE id_activo = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getTipo());
            ps.setString(3, a.getIp());
            ps.setString(4, a.getUbicacion());
            ps.setString(5, a.getResponsable());
            ps.setString(6, a.getNivelCriticidad());
            ps.setString(7, a.getEstado());
            ps.setInt(8, a.getIdActivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idActivo) {
        String sql = "DELETE FROM activos WHERE id_activo = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idActivo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Activo mapear(ResultSet rs) throws SQLException {
        Activo a = new Activo();
        a.setIdActivo(rs.getInt("id_activo"));
        a.setCodigo(rs.getString("codigo"));
        a.setNombre(rs.getString("nombre"));
        a.setTipo(rs.getString("tipo"));
        a.setIp(rs.getString("ip"));
        a.setUbicacion(rs.getString("ubicacion"));
        a.setResponsable(rs.getString("responsable"));
        a.setNivelCriticidad(rs.getString("nivel_criticidad"));
        a.setEstado(rs.getString("estado"));
        a.setFechaRegistro(rs.getString("fecha_registro"));
        return a;
    }
}
