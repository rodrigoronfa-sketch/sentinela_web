package com.sentinel.model;

import java.io.Serializable;

public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idRol;
    private String nombre = "";
    private String descripcion = "";

    public Rol() {
    }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
