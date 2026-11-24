package com.sentinel.model;

import java.io.Serializable;

public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idAsistencia;
    private String codigo = "";
    private int idEmpleado;
    private String nombreEmpleado = "";
    private String fecha = "";
    private String horaEntrada = "";
    private String horaSalida = "";
    private String estado = "Presente";
    private String observaciones = "";

    public Asistencia() {
    }

    public int getIdAsistencia() { return idAsistencia; }
    public void setIdAsistencia(int idAsistencia) { this.idAsistencia = idAsistencia; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(String horaEntrada) { this.horaEntrada = horaEntrada; }

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    /** Clase CSS para la etiqueta de estado. */
    public String getEstadoClase() {
        if ("Presente".equals(estado)) return "ok";
        if ("Ausente".equals(estado)) return "critica";
        if ("Retrasado".equals(estado)) return "alta";
        if ("Justificada".equals(estado)) return "info";
        return "gris";
    }

    public String getEmpleadoTexto() {
        return nombreEmpleado == null ? "" : nombreEmpleado;
    }
}
