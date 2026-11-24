package com.sentinel.dao;

import com.sentinel.model.Asistencia;
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

public class AsistenciaDAO {

    private static final String SELECT =
            "SELECT a.*, "
            + "u.nombres AS nom_empleado, u.apellidos AS ape_empleado "
            + "FROM asistencias a "
            + "LEFT JOIN usuarios u ON a.id_empleado = u.id_usuario ";

    /**
     * El administrador ve todas. El personal solo ve las suyas.
     */
    public List<Asistencia> listar(String estado, Usuario usuario) {
        List<Asistencia> lista = new ArrayList<Asistencia>();
        StringBuilder sql = new StringBuilder(SELECT);
        sql.append("WHERE 1 = 1 ");
        List<Object> parametros = new ArrayList<Object>();

        if (usuario != null && !Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            sql.append("AND a.id_empleado = ? ");
            parametros.add(usuario.getIdUsuario());
        }
        if (estado != null && !estado.trim().isEmpty()) {
            sql.append("AND a.estado = ? ");
            parametros.add(estado);
        }
        sql.append("ORDER BY a.fecha DESC, a.hora_entrada DESC");

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

    public List<Asistencia> listarRecientes(int limite) {
        List<Asistencia> lista = new ArrayList<Asistencia>();
        String sql = SELECT + "ORDER BY a.fecha DESC, a.hora_entrada DESC LIMIT ?";
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

    public Asistencia obtener(int idAsistencia) {
        String sql = SELECT + "WHERE a.id_asistencia = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAsistencia);
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
        String sql = "SELECT CONCAT('ASI-', YEAR(CURDATE()), '-', LPAD("
                + "COALESCE(MAX(CAST(SUBSTRING_INDEX(codigo, '-', -1) AS UNSIGNED)), 0) + 1, 4, '0')) "
                + "AS codigo FROM asistencias";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("codigo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ASI-" + java.time.Year.now().getValue() + "-0001";
    }

    public boolean crear(Asistencia a) {
        String codigo = siguienteCodigo();
        a.setCodigo(codigo);
        String sql = "INSERT INTO asistencias "
                + "(codigo, id_empleado, fecha, hora_entrada, hora_salida, estado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, a.getIdEmpleado());
            ps.setDate(3, fechaComoDate(a.getFecha()));
            setHora(ps, 4, a.getHoraEntrada());
            setHora(ps, 5, a.getHoraSalida());
            String estado = a.getEstado();
            ps.setString(6, (estado == null || estado.trim().isEmpty()) ? "Presente" : estado);
            ps.setString(7, a.getObservaciones());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Asistencia a) {
        String sql = "UPDATE asistencias SET id_empleado = ?, fecha = ?, "
                + "hora_entrada = ?, hora_salida = ?, estado = ?, observaciones = ? "
                + "WHERE id_asistencia = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setDate(2, fechaComoDate(a.getFecha()));
            setHora(ps, 3, a.getHoraEntrada());
            setHora(ps, 4, a.getHoraSalida());
            ps.setString(5, a.getEstado());
            ps.setString(6, a.getObservaciones());
            ps.setInt(7, a.getIdAsistencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstado(int idAsistencia, String estado) {
        String sql = "UPDATE asistencias SET estado = ? WHERE id_asistencia = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idAsistencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idAsistencia) {
        String sql = "DELETE FROM asistencias WHERE id_asistencia = ?";
        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAsistencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private java.sql.Date fechaComoDate(String fecha) {
        String f = fecha == null ? "" : fecha.trim();
        if (!f.isEmpty()) {
            try {
                return java.sql.Date.valueOf(f);
            } catch (IllegalArgumentException iae) {
                // Formato inesperado: se usa la fecha de hoy
            }
        }
        return java.sql.Date.valueOf(java.time.LocalDate.now());
    }

    private void setHora(PreparedStatement ps, int parametro, String hora) throws SQLException {
        String h = hora == null ? "" : hora.trim();
        if (h.isEmpty()) {
            ps.setNull(parametro, Types.TIME);
        } else {
            ps.setString(parametro, h);
        }
    }

    private Asistencia mapear(ResultSet rs) throws SQLException {
        Asistencia a = new Asistencia();
        a.setIdAsistencia(rs.getInt("id_asistencia"));
        a.setCodigo(rs.getString("codigo"));
        a.setIdEmpleado(rs.getInt("id_empleado"));
        a.setNombreEmpleado(unir(rs.getString("nom_empleado"), rs.getString("ape_empleado")));
        a.setFecha(nvl(rs.getString("fecha")));
        a.setHoraEntrada(nvl(rs.getString("hora_entrada")));
        a.setHoraSalida(nvl(rs.getString("hora_salida")));
        a.setEstado(nvl(rs.getString("estado")));
        a.setObservaciones(rs.getString("observaciones"));
        return a;
    }

    private String unir(String a, String b) {
        String uno = a == null ? "" : a;
        String dos = b == null ? "" : b;
        return (uno + " " + dos).trim();
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }
}
