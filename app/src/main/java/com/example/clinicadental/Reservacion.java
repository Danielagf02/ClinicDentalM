package com.example.clinicadental;

public class Reservacion {

    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;

    private String fecha_reservacion;
    private String hora_reservacion;

    public Reservacion(){

    }

    public Reservacion(String nombre, String apellido_paterno, String apellido_materno,  String fecha_reservacion, String hora_reservacion) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_reservacion = fecha_reservacion;
        this.hora_reservacion = hora_reservacion;
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

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    //public String getNumero_personas() {
      //  return numero_personas;
    //}

    //public void setNumero_personas(String numero_personas) {
      //  this.numero_personas = numero_personas;
    //}

    //public String getNumero_mesas() {
      //  return numero_mesas;
    //}

    //public void setNumero_mesas(String numero_mesas) {
      //  this.numero_mesas = numero_mesas;
    //}

    public String getFecha_reservacion() {
        return fecha_reservacion;
    }

    public void setFecha_reservacion(String fecha_reservacion) {
        this.fecha_reservacion = fecha_reservacion;
    }

    public String getHora_reservacion() {
        return hora_reservacion;
    }

    public void setHora_reservacion(String hora_reservacion) {
        this.hora_reservacion = hora_reservacion;
    }
}
