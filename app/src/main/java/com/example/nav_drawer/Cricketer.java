package com.example.nav_drawer;

import java.io.Serializable;

public class Cricketer implements Serializable {

    private String spinner1;
    private String editDosis;
    private String editDuracion;
    private String editIntervalo;
    private String editEspecificaciones;

    public Cricketer() {

    }

    public Cricketer(String spinner1, String editDosis, String editDuracion, String editIntervalo, String editEspecificaciones) {
        this.spinner1 = spinner1;
        this.editDosis = editDosis;
        this.editDuracion = editDuracion;
        this.editIntervalo = editIntervalo;
        this.editEspecificaciones = editEspecificaciones;
    }

    public String getSpinner1() {
        return spinner1;
    }

    public void setSpinner1(String spinner1) {
        this.spinner1 = spinner1;
    }

    public String getEditDosis() {
        return editDosis;
    }

    public void setEditDosis(String editDosis) {
        this.editDosis = editDosis;
    }

    public String getEditDuracion() {
        return editDuracion;
    }

    public void setEditDuracion(String editDuracion) {
        this.editDuracion = editDuracion;
    }

    public String getEditIntervalo() {
        return editIntervalo;
    }

    public void setEditIntervalo(String editIntervalo) {
        this.editIntervalo = editIntervalo;
    }

    public String getEditEspecificaciones() {
        return editEspecificaciones;
    }

    public void setEditEspecificaciones(String editEspecificaciones) {
        this.editEspecificaciones = editEspecificaciones;
    }
}
