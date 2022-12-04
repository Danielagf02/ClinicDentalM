package com.example.clinicadental;

public class Tratamiento {

    private String nombre;
    private String descripcion;
    private String precio;
    private String link_imagen;

    public Tratamiento(){

    }

    public Tratamiento(String nombre, String descripcion, String precio, String link_imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.link_imagen = link_imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getLink_imagen() {
        return link_imagen;
    }

    public void setLink_imagen(String link_imagen) {
        this.link_imagen = link_imagen;
    }
}
