package com.tgm.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String rol;
    private String password;

    //Constructor vacío
    public Usuario() {}

    //Constructor para nuevos usuarios
    public Usuario(String nombre, String correo, String rol, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.password = password;
    }

    //Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String  getPassword() { return password; }
    public void setPassword (String password) { this.password = password; }
}
