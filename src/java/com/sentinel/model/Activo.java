package com.sentinel.model;

import java.io.Serializable;

public class Activo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idActivo;
    private String codigo = "";
    private String nombre = "";
    private String tipo = "";
    private String ip = "";
    private String ubicacion = "";
    private String responsable = "";
    private String nivelCriticidad = "Medio";
    private String estado = "Operativo";
    private String fechaRegistro = "";

    public Activo() {
    }

    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int idActivo) { this.idActivo = idActivo; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getNivelCriticidad() { return nivelCriticidad; }
    public void setNivelCriticidad(String nivelCriticidad) { this.nivelCriticidad = nivelCriticidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstadoClase() {
        if ("Operativo".equals(estado)) return "ok";
        if ("En mantenimiento".equals(estado)) return "media";
        if ("En riesgo".equals(estado)) return "critica";
        if ("Fuera de servicio".equals(estado)) return "danger";
        return "gris";
    }

    public String getCriticidadClase() {
        if ("Crítico".equals(nivelCriticidad)) return "critica";
        if ("Alto".equals(nivelCriticidad)) return "alta";
        if ("Medio".equals(nivelCriticidad)) return "media";
        if ("Bajo".equals(nivelCriticidad)) return "baja";
        return "gris";
    }
}
