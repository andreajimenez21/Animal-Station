package com.aj.refugio.model;

public class Persona {
    //Variables principales
    private String nombre;
    private String dni;
    private String telefono;

    //Constructor
    public Persona(String nombre, String dni, String telefono) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
    }

    //Metodos

    public String getNombre() {
        return this.nombre;
    }

    public String getDni() {
        return this.dni;
    }

    public String getTelefono() {
        return this.telefono;
    }

    // Añadido toString() para que al imprimir una persona se vean sus datos
    @Override
    public String toString() {
        return "Nombre: " + nombre +
               " | DNI: " + dni +
               " | Teléfono: " + telefono;
    }
}
