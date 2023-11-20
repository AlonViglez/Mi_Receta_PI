package com.example.nav_drawer;

public class DoctorModel {
    private String correo;
    private Long fechanac;
    private String id;
    private String nombre;
    private String password;
    private String sexo;

    public DoctorModel(){

    }

    public DoctorModel(String correo, Long fechanac, String id, String nombre, String password, String sexo) {
        this.correo = correo;
        this.fechanac = fechanac;
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.sexo = sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getFechanac() {
        return fechanac;
    }

    public void setFechanac(Long fechanac) {
        this.fechanac = fechanac;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}