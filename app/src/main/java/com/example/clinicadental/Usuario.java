package com.example.clinicadental;

public class Usuario {

    private String nombre;
    private String apellido_paterno;
    private String correo;
    private String link_imagen;

    public Usuario(){

    }

    /*public Usuario(String nombre, String apellido_paterno, String correo) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.correo = correo;
    }*/

    public Usuario(String nombre, String apellido_paterno, String correo, String link_imagen) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.correo = correo;
        this.link_imagen = link_imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getLink_imagen() {
        return link_imagen;
    }

    public void setLink_imagen(String link_imagen) {
        this.link_imagen = link_imagen;
    }
}
