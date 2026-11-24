package com.sentinel.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String usuario = "";
    private String password = "";
    private String nombres = "";
    private String apellidos = "";
    private String email = "";
    private int idRol;
    private String nombreRol = "";
    private boolean activo = true;
    private String foto = "imagenes/sin-foto.svg";
    private String fechaCreacion = "";
    private String ultimoAcceso = "";

    public Usuario() {
    }

    private String seguro(String valor) {
        return valor == null ? "" : valor;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = seguro(usuario); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = seguro(password); }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = seguro(nombres); }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = seguro(apellidos); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = seguro(email); }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = seguro(nombreRol); }

    public boolean isActivo() { return activo; }
    public boolean getActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = seguro(foto).isEmpty() ? "imagenes/sin-foto.svg" : seguro(foto); }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = seguro(fechaCreacion); }

    public String getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(String ultimoAcceso) { this.ultimoAcceso = seguro(ultimoAcceso); }

    public String getNombreCompleto() {
        return (nombres + " " + apellidos).trim();
    }
}
