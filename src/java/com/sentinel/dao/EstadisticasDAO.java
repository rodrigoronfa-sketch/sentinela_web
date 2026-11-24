package com.sentinel.dao;

import com.sentinel.model.Estadisticas;
import com.sentinel.model.Usuario;
import com.sentinel.util.Constantes;
import com.sentinel.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadisticasDAO {

    public Estadisticas obtener() {
        Estadisticas e = new Estadisticas();
        String sql =
              "SELECT "
            + "(SELECT COUNT(*) FROM asistencias) AS total_asist, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Presente') AS presentes, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Ausente') AS ausentes, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Retrasado') AS retrasados, "
            + "(SELECT COUNT(*) FROM activos) AS total_activos, "
            + "(SELECT COUNT(*) FROM activos WHERE estado IN ('En riesgo', 'Fuera de servicio')) AS activos_riesgo, "
            + "(SELECT COUNT(*) FROM usuarios) AS total_usuarios";

        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                e.setTotalAsistencias(rs.getInt("total_asist"));
                e.setPresentes(rs.getInt("presentes"));
                e.setAusentes(rs.getInt("ausentes"));
                e.setRetrasados(rs.getInt("retrasados"));
                e.setTotalActivos(rs.getInt("total_activos"));
                e.setActivosRiesgo(rs.getInt("activos_riesgo"));
                e.setTotalUsuarios(rs.getInt("total_usuarios"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return e;
    }

    /** Estadisticas filtradas: el personal solo ve sus propias asistencias. */
    public Estadisticas obtenerPara(Usuario usuario) {
        if (usuario == null || Constantes.ROL_ADMIN.equals(usuario.getNombreRol())) {
            return obtener();
        }

        Estadisticas e = new Estadisticas();
        String sql =
              "SELECT "
            + "(SELECT COUNT(*) FROM asistencias WHERE id_empleado = ?) AS total_asist, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Presente' AND id_empleado = ?) AS presentes, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Ausente' AND id_empleado = ?) AS ausentes, "
            + "(SELECT COUNT(*) FROM asistencias WHERE estado = 'Retrasado' AND id_empleado = ?) AS retrasados";

        try (Connection cn = ConexionBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            int i = 1;
            for (int bloque = 0; bloque < 4; bloque++) {
                ps.setInt(i++, usuario.getIdUsuario());
                ps.setInt(i++, usuario.getIdUsuario());
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e.setTotalAsistencias(rs.getInt("total_asist"));
                    e.setPresentes(rs.getInt("presentes"));
                    e.setAusentes(rs.getInt("ausentes"));
                    e.setRetrasados(rs.getInt("retrasados"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        e.setTotalActivos(new ActivoDAO().listar(null, null).size());
        e.setActivosRiesgo(0);
        e.setTotalUsuarios(0);
        return e;
    }
}
