package com.aj.refugio.model;

import java.time.LocalDate;
import java.util.ArrayList;

// Representa una donación hecha al refugio (dinero, comida, medicinas, etc.)
// También guarda un historial de cantidades si el mismo donante dona varias veces.
public class Donacion {

    // Nombre de la persona que dona
    private String donante;

    // Cantidad inicial de la donación
    private double cantidad;

    // Fecha en la que se hizo la donación
    private LocalDate fecha;

    // Tipo de donación: dinero, comida, medicinas…
    private String tipo;

    // Lista con todas las cantidades donadas por este donante
    private ArrayList<Double> donaciones;

    // Constructor principal: crea la donación y la añade al historial
    public Donacion(String donante, double cantidad, LocalDate fecha, String tipo) {
        this.donante  = donante;
        this.cantidad = cantidad;
        this.fecha    = fecha;
        this.tipo     = tipo;

        // Inicializamos la lista y guardamos la primera donación
        this.donaciones = new ArrayList<>();
        this.donaciones.add(cantidad);
    }

    // Añade una nueva cantidad al historial de este donante
    public void agregarDonacion(double cantidad) {
        this.donaciones.add(cantidad);
    }

    // Devuelve la lista completa de cantidades donadas
    public ArrayList<Double> getDonaciones() {
        return this.donaciones;
    }

    // Suma todas las cantidades del historial y devuelve el total
    public double getTotalDonado() {
        double total = 0;
        for (double d : donaciones) {
            total += d;
        }
        return total;
    }

    // Getters básicos
    public String    getDonante()  { return this.donante; }
    public double    getCantidad() { return this.cantidad; }
    public LocalDate getFecha()    { return this.fecha; }
    public String    getTipo()     { return this.tipo; }

    // Representación en texto de la donación (se usa en mensajes y tablas)
    @Override
    public String toString() {
        return "Donante: " + donante +
               " | Cantidad: " + cantidad +
               " | Total donado: " + getTotalDonado() +
               " | Tipo: " + tipo +
               " | Fecha: " + fecha;
    }

    // Genera una línea JSON con los datos de la donación (usado al exportar)
    public String toJSON() {
        return "{\"donante\":\"" + donante + "\"" +
               ",\"cantidad\":"  + cantidad +
               ",\"tipo\":\""    + tipo     + "\"" +
               ",\"fecha\":\""   + fecha    + "\"}";
    }

    // Genera una línea CSV con los datos de la donación (usado al exportar)
    public String toCSV() {
        return donante + "," + cantidad + "," + tipo + "," + fecha;
    }
}
