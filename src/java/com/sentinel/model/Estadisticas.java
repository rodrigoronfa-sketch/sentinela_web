package com.sentinel.model;

import java.io.Serializable;

public class Estadisticas implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalAsistencias;
    private int presentes;
    private int ausentes;
    private int retrasados;
    private int totalActivos;
    private int activosRiesgo;
    private int totalUsuarios;

    public Estadisticas() {
    }

    public int getTotalAsistencias() { return totalAsistencias; }
    public void setTotalAsistencias(int totalAsistencias) { this.totalAsistencias = totalAsistencias; }

    public int getPresentes() { return presentes; }
    public void setPresentes(int presentes) { this.presentes = presentes; }

    public int getAusentes() { return ausentes; }
    public void setAusentes(int ausentes) { this.ausentes = ausentes; }

    public int getRetrasados() { return retrasados; }
    public void setRetrasados(int retrasados) { this.retrasados = retrasados; }

    public int getTotalActivos() { return totalActivos; }
    public void setTotalActivos(int totalActivos) { this.totalActivos = totalActivos; }

    public int getActivosRiesgo() { return activosRiesgo; }
    public void setActivosRiesgo(int activosRiesgo) { this.activosRiesgo = activosRiesgo; }

    public int getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }
}
