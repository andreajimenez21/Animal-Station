package com.aj.refugio;

import java.time.LocalDate;
import java.util.Scanner;

import com.aj.refugio.model.Adoptante;
import com.aj.refugio.model.Animal;
import com.aj.refugio.model.AnimalDomestico;
import com.aj.refugio.model.AnimalSalvaje;
import com.aj.refugio.model.Donacion;
import com.aj.refugio.model.Empleado;
import com.aj.refugio.service.Refugio;

public class App {

    static Scanner scanner = new Scanner(System.in);
    static Refugio refugio = new Refugio("Libertad", "Calle Desengaño 21");
    // ID para animales
    static int nextId = 1;

    public static void main(String[] args) {

        int opcion;
        do {
            System.out.println("\n====== REFUGIO " + refugio.getNombre() + " ======");
            System.out.println("1. Animales");
            System.out.println("2. Adoptantes");
            System.out.println("3. Donaciones");
            System.out.println("4. Empleados");
            System.out.println("5. Salir");
            System.out.println("6. Importar CSV");
            System.out.println("7. Exportar CSV");
            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1 ->
                    menuAnimales();
                case 2 ->
                    menuAdoptantes();
                case 3 ->
                    menuDonaciones();
                case 4 ->
                    menuEmpleados();
                case 6 ->
                    importarCSVConsola();
                case 7 ->
                    exportarCSVConsola();
                case 5 ->
                    System.out.println("Hasta pronto!");
                default ->
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 5);

        scanner.close();
    }

    // ── ANIMALES
    static void menuAnimales() {
        int op;
        do {
            System.out.println("\n-- ANIMALES --");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Añadir animal domestico");
            System.out.println("4. Añadir animal salvaje");
            System.out.println("5. Eliminar animal");
            System.out.println("0. Volver");
            op = leerEntero("Opcion: ");

            switch (op) {
                case 1 -> {
                    // Solo se muestran domésticos no adoptados y salvajes no liberados
                    boolean hayDisponibles = false;
                    for (Animal a : refugio.animales) {
                        if (a instanceof AnimalDomestico ad && ad.isAdoptado()) continue;
                        if (a instanceof AnimalSalvaje as && as.isLiberado()) continue;
                        System.out.println(a);
                        hayDisponibles = true;
                    }
                    if (!hayDisponibles) {
                        System.out.println("No hay animales disponibles en el refugio.");
                    }
                }
                case 2 -> {
                    int id = leerEntero("ID del animal: ");
                    Animal a = refugio.buscarAnimalPorID(id);
                    System.out.println(a != null ? a : "No encontrado.");
                }
                case 3 -> {
                    System.out.println("-- Nuevo animal domestico --");
                    String nombre = leerTexto("Nombre: ");
                    String especie = leerTexto("Especie: ");
                    int edad = leerEntero("Edad: ");
                    double peso = leerDecimal("Peso (kg): ");
                    boolean sociable = leerBooleano("¿Sociable? (s/n): ");
                    boolean vacunado = leerBooleano("¿Vacunado? (s/n): ");
                    refugio.agregarAnimal(new AnimalDomestico(nextId++, nombre, especie, edad, peso, false, sociable, vacunado));
                    System.out.println("Animal añadido con ID " + (nextId - 1));
                }
                case 4 -> {
                    System.out.println("-- Nuevo animal salvaje --");
                    String nombre = leerTexto("Nombre: ");
                    String especie = leerTexto("Especie: ");
                    int edad = leerEntero("Edad: ");
                    double peso = leerDecimal("Peso (kg): ");
                    String habitat = leerTexto("Habitat de origen: ");
                    boolean peligroso = leerBooleano("¿Peligroso? (s/n): ");
                    boolean protegido = leerBooleano("¿Protegido? (s/n): ");
                    boolean liberado = leerBooleano("¿Ya liberado? (s/n): ");
                    refugio.agregarAnimal(new AnimalSalvaje(nextId++, nombre, especie, edad, peso, habitat, peligroso, protegido, liberado));
                    System.out.println("Animal añadido con ID " + (nextId - 1));
                }
                case 5 -> {
                    int id = leerEntero("ID del animal a eliminar: ");
                    Animal a = refugio.buscarAnimalPorID(id);
                    if (a != null) {
                        refugio.eliminarAnimal(id);
                        System.out.println("Animal eliminado.");
                    } else {
                        System.out.println("No encontrado.");
                    }
                }
                case 0 -> {
                }
                default ->
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ── ADOPTANTES 
    static void menuAdoptantes() {
        int op;
        do {
            System.out.println("\n-- ADOPTANTES --");
            System.out.println("1. Ver adoptantes");
            System.out.println("2. Registrar adoptante");
            System.out.println("3. Adoptar un animal");
            System.out.println("0. Volver");
            op = leerEntero("Opcion: ");

            switch (op) {
                case 1 -> {
                    if (refugio.adoptantes.isEmpty()) {
                        System.out.println("No hay adoptantes registrados.");
                    } else {
                        for (var p : refugio.adoptantes) {
                            if (p instanceof Adoptante) {
                                System.out.println(p);
                            }
                        }
                    }
                }
                case 2 -> {
                    String nombre = leerTexto("Nombre: ");
                    String dni = leerTexto("DNI: ");
                    String telefono = leerTexto("Telefono: ");
                    refugio.adoptantes.add(new Adoptante(nombre, dni, telefono));
                    System.out.println("Adoptante registrado.");
                }
                case 3 -> {
                    var adoptantes = refugio.adoptantes.stream()
                            .filter(p -> p instanceof Adoptante)
                            .map(p -> (Adoptante) p)
                            .toList();

                    if (adoptantes.isEmpty()) {
                        System.out.println("No hay adoptantes. Registra uno primero.");
                        break;
                    }
                    System.out.println("Adoptantes:");
                    for (int i = 0; i < adoptantes.size(); i++) {
                        System.out.println((i + 1) + ". " + adoptantes.get(i).getNombre());
                    }
                    int idx = leerEntero("Selecciona adoptante (numero): ") - 1;
                    if (idx < 0 || idx >= adoptantes.size()) {
                        System.out.println("Seleccion no valida.");
                        break;
                    }

                    System.out.println("Animales disponibles para adopcion:");
                    boolean hay = false;
                    for (Animal a : refugio.animales) {
                        if (a instanceof AnimalDomestico ad && !ad.isAdoptado()) {
                            System.out.println("  ID " + a.getId() + ": " + a.getNombre() + " (" + a.getEspecie() + ")");
                            hay = true;
                        }
                    }
                    if (!hay) {
                        System.out.println("No hay animales disponibles.");
                        break;
                    }

                    int idAnimal = leerEntero("ID del animal: ");
                    Animal animal = refugio.buscarAnimalPorID(idAnimal);

                    if (animal == null || !(animal instanceof AnimalDomestico)) {
                        System.out.println("Animal no valido.");
                    } else if (((AnimalDomestico) animal).isAdoptado()) {
                        System.out.println("Ese animal ya fue adoptado.");
                    } else {
                        String resultado = refugio.adoptarAnimal(adoptantes.get(idx).getDni(), idAnimal);
                        System.out.println(resultado);
                    }
                }
                case 0 -> {
                }
                default ->
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ── DONACIONES 
    static void menuDonaciones() {
        int op;
        do {
            System.out.println("\n-- DONACIONES --");
            System.out.println("1. Ver donaciones");
            System.out.println("2. Registrar donacion");
            System.out.println("3. Ver total recaudado");
            System.out.println("0. Volver");
            op = leerEntero("Opcion: ");

            switch (op) {
                case 1 -> {
                    if (refugio.donaciones.isEmpty()) {
                        System.out.println("No hay donaciones registradas.");
                    } else {
                        for (Donacion d : refugio.donaciones) {
                            System.out.println(d);
                        }
                    }
                }
                case 2 -> {
                    String donante = leerTexto("Nombre del donante: ");
                    double cantidad = leerDecimal("Cantidad: ");
                    String tipo = leerTexto("Tipo (Dinero/Comida/Medicamentos/Otro): ");
                    refugio.registrarDonacion(new Donacion(donante, cantidad, LocalDate.now(), tipo));
                    System.out.println("Donacion registrada.");
                }
                case 3 ->
                    System.out.println("Total recaudado: " + refugio.calcularTotalDonaciones() + " euros");
                case 0 -> {
                }
                default ->
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ── EMPLEADOS 
    static void menuEmpleados() {
        int op;
        do {
            System.out.println("\n-- EMPLEADOS --");
            System.out.println("1. Ver empleados");
            System.out.println("2. Registrar empleado");
            System.out.println("0. Volver");
            op = leerEntero("Opcion: ");

            switch (op) {
                case 1 -> {
                    boolean hay = false;
                    for (var p : refugio.adoptantes) {
                        if (p instanceof Empleado e) {
                            System.out.println(e);
                            hay = true;
                        }
                    }
                    if (!hay) {
                        System.out.println("No hay empleados registrados.");
                    }
                }
                case 2 -> {
                    String nombre = leerTexto("Nombre: ");
                    String dni = leerTexto("DNI: ");
                    String telefono = leerTexto("Telefono: ");
                    int idEmp = leerEntero("ID empleado: ");
                    String tipo = leerTexto("Tipo (Veterinario/Cuidador/Administrativo): ");
                    String cargo = leerTexto("Animales a cargo (Domesticos/Salvajes/Ambos): ");
                    refugio.adoptantes.add(new Empleado(nombre, dni, telefono, idEmp, tipo, cargo));
                    System.out.println("Empleado registrado.");
                }
                case 0 -> {
                }
                default ->
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ── UTILIDADES 
    static int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero entero.");
            }
        }
    }

    static double leerDecimal(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero valido.");
            }
        }
    }

    static String leerTexto(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    static boolean leerBooleano(String msg) {
        while (true) {
            System.out.print(msg);
            String r = scanner.nextLine().trim().toLowerCase();
            if (r.equals("s")) {
                return true;
            }
            if (r.equals("n")) {
                return false;
            }
            System.out.println("Escribe s o n.");
        }
    }

    // ── IMPORTAR CSV DESDE CONSOLA ───────────────────────────────
    static void importarCSVConsola() {
        String ruta = leerTexto("Carpeta que contiene los CSV: ");
        String resultado = refugio.impCSV(java.nio.file.Path.of(ruta));
        System.out.println(resultado);
    }

    // ── EXPORTAR CSV DESDE CONSOLA ───────────────────────────────
    static void exportarCSVConsola() {
        String ruta = leerTexto("Carpeta donde guardar los CSV: ");
        String resultado = refugio.expCSV(java.nio.file.Path.of(ruta));
        System.out.println(resultado);
    }

}
