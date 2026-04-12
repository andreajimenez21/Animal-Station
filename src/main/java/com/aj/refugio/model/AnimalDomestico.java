package com.aj.refugio.model;

// Esta clase representa animales domésticos: perros, gatos, conejos, etc.
// Extiende de Animal porque comparte los atributos básicos.
public class AnimalDomestico extends Animal {

    // Indica si el animal se lleva bien con personas u otros animales
    private boolean sociable;

    // Indica si tiene las vacunas al día
    private boolean vacunado;

    private boolean adoptado;

    // Constructor 
    //Añadimos adoptado, sociable y vacunado al constructor 
    public AnimalDomestico(int id, String nombre, String especie, int edad, double peso,
            boolean adoptado, boolean sociable, boolean vacunado) {

        // Llamamos al constructor de la clase padre (Animal)
        super(id, nombre, especie, edad, peso);

        this.adoptado = adoptado;
        this.sociable = sociable;
        this.vacunado = vacunado;
    }

    // Devuelve el tipo de animal
    @Override
    public String getTipo() {
        return "Doméstico";
    }

    // Representación en texto del animal doméstico
    @Override
    public String toString() {
        return "ID: " + getId()
                + " | Nombre: " + getNombre()
                + " | Especie: " + getEspecie()
                + " | Edad: " + getEdad()
                + " | Peso: " + getPeso()
                + " | Adoptado: " + (adoptado ? "Sí" : "No")
                + " | Sociable: " + (sociable ? "Sí" : "No")
                + " | Vacunado: " + (vacunado ? "Sí" : "No");
    }

    public void setAdoptado(boolean adoptado) {
        this.adoptado = adoptado;
    }

    public boolean isAdoptado() {
        return adoptado;
    }

    public boolean isSociable() {
        return sociable;
    }

    public boolean isVacunado() {
        return vacunado;
    }

    }
}
