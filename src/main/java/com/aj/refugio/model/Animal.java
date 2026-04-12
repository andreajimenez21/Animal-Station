package com.aj.refugio.model;



public abstract class Animal {
    //Variables principales
    private int id;
    private String nombre;
    private String especie;
    private int edad;
    private double peso;
    private String tipo = "desconocido"; // Cambiar por doméstico o salvaje con el "setTipo()"
    //Constructor
    public Animal(int id, String nombre, String especie, int edad, double peso) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.peso = peso;

    }

    //Metodos
    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEspecie() {
        return this.especie;
    }

    public int getEdad() {
        return this.edad;
    }

    public double getPeso() {
        return this.peso;
    }

    // Cada subclase dirá si es Doméstico o Salvaje
    public String getTipo() {
        return this.tipo;
    }


    // Cada subclase decide cómo se muestra
    @Override
    public abstract String toString();
}

