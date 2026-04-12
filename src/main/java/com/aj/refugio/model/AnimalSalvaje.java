package com.aj.refugio.model;

// Esta clase representa animales salvajes que llegan al refugio.
// Son animales que NO están domesticados: zorros, búhos, jabalíes, etc.
// Extiende de Animal porque comparte los atributos básicos.
public class AnimalSalvaje extends Animal {

    // Lugar de donde proviene el animal (bosque, montaña, selva…)
    private String habitatOrigen;

    // Indica si puede ser peligroso para personas u otros animales
    private boolean peligroso;

    // Indica si pertenece a una especie protegida
    private boolean protegido;

    // Indica si el animal ya fue liberado o sigue en el refugio
    private boolean liberado;

    // Constructor 
    // Añadimos liberado al constructor para que no siempre arranque en false sin querer
    public AnimalSalvaje(int id, String nombre, String especie, int edad, double peso,
                         String habitatOrigen, boolean peligroso, boolean protegido, boolean liberado) {

        // Llamamos al constructor de la clase padre para inicializar los atributos comunes
        super(id, nombre, especie, edad, peso);

        this.habitatOrigen = habitatOrigen;
        this.peligroso = peligroso;
        this.protegido = protegido;
        this.liberado = liberado;
    }

    // Devuelve el tipo de animal (útil para listados)
    @Override
    public String getTipo() {
        return "Salvaje";
    }

    // Representación en texto del animal salvaje
    @Override
    public String toString() {
        return "ID: " + getId() +
               " | Nombre: " + getNombre() +
               " | Especie: " + getEspecie() +
               " | Edad: " + getEdad() +
               " | Peso: " + getPeso() +
               " | Hábitat: " + habitatOrigen +
               " | Peligroso: " + (peligroso ? "Sí" : "No") +
               " | Protegido: " + (protegido ? "Sí" : "No") +
               " | Liberado: " + (liberado ? "Sí" : "No");
    }

    // Cambiamos esLiberado() por isLiberado() para ser consistentes con los demás booleans
    public boolean isLiberado() {
        return liberado;
    }

    public boolean isPeligroso() {
        return peligroso;
    }

    public boolean isProtegido() {
        return protegido;
    }

    public String getHabitatOrigen() {
        return habitatOrigen;
    }

    }
}