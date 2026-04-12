package com.aj.refugio.model;

public class Empleado extends Persona {

    //Variables principales
    private int idEmpleado;
    private String tipoEmpleado; // administrativo, veterinario, cuidador
    private String tipoAnimalCargo; // domestico, salvaje, ambos

    //Constructor
    public Empleado(String nombre, String dni, String telefono, int idEmpleado, String tipoEmpleado, String tipoAnimalCargo) {
        super(nombre, dni, telefono);
        this.idEmpleado = idEmpleado;
        this.tipoEmpleado = tipoEmpleado;
        this.tipoAnimalCargo = tipoAnimalCargo;
    }

    //Metodos

    public int getIdEmpleado() {
        return this.idEmpleado;
    }

    public String getTipoEmpleado() {
        return this.tipoEmpleado;
    }

    public String getTipoAnimalCargo() {
        return this.tipoAnimalCargo;
    }

    // Añadido toString() para que al imprimir un empleado se vean sus datos
    @Override
    public String toString() {
        return "Empleado: " + getNombre() +
               " | DNI: " + getDni() +
               " | Teléfono: " + getTelefono() +
               " | ID Empleado: " + idEmpleado +
               " | Tipo: " + tipoEmpleado +
               " | Animales a cargo: " + tipoAnimalCargo;
    }
}