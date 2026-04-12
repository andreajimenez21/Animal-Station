package com.aj.refugio.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import com.aj.refugio.model.Adoptante;
import com.aj.refugio.model.Animal;
import com.aj.refugio.model.AnimalDomestico;
import com.aj.refugio.model.AnimalSalvaje;
import com.aj.refugio.model.Donacion;
import com.aj.refugio.model.Empleado;
import com.aj.refugio.model.Persona;

public class Refugio implements Exportable {

    // ============================================================
    // VARIABLES PRINCIPALES
    // ============================================================
    private String nombre;
    private String direccion;

    public ArrayList<Animal> animales;
    public ArrayList<Donacion> donaciones;
    public ArrayList<Persona> adoptantes; // Adoptantes + Empleados

    // ============================================================
    // CONSTRUCTORES
    // ============================================================
    public Refugio() {
        this("Refugio AnimalStation", "Sin dirección");
    }

    public Refugio(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.animales = new ArrayList<>();
        this.donaciones = new ArrayList<>();
        this.adoptantes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    // ============================================================
    // ANIMALES
    // ============================================================
    public void agregarAnimal(Animal a) {
        this.animales.add(a);
    }

    // Devuelve la lista completa de animales del refugio
    public ArrayList<Animal> listarAnimales() {
        return this.animales;
    }

    // Versión simplificada: crea un AnimalDomestico genérico a partir de los datos básicos.
    // El ID se genera automáticamente sumando 1 al mayor ID existente.
    // Usada desde la GUI cuando el usuario añade un animal sin especificar todos los detalles.
    public void añadirAnimal(String nombre, String especie, int edad, double peso, String tipo) {
        int nuevoId = animales.stream().mapToInt(Animal::getId).max().orElse(0) + 1;
        Animal a;
        if (tipo.equalsIgnoreCase("salvaje")) {
            a = new AnimalSalvaje(nuevoId, nombre, especie, edad, peso,
                    "desconocido", false, false, false);
        } else {
            // Por defecto se crea como doméstico (tipo "domestico" u otro)
            a = new AnimalDomestico(nuevoId, nombre, especie, edad, peso,
                    false, false, false);
        }
        this.animales.add(a);
    }

    public boolean eliminarAnimal(int id) {
        return this.animales.removeIf(animal -> animal.getId() == id);
    }

    public Animal buscarAnimalPorID(int id) {
        for (Animal animal : this.animales) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null;
    }

    // ============================================================
    // ADOPTANTES
    // ============================================================
    public void añadirAdoptante(String nombre, String dni, String telefono) {
        adoptantes.add(new Adoptante(nombre, dni, telefono));
    }

    public Adoptante buscarAdoptantePorDni(String dni) {
        for (Persona p : adoptantes) {
            if (p instanceof Adoptante a && a.getDni().equalsIgnoreCase(dni)) {
                return a;
            }
        }
        return null;
    }

    public boolean eliminarAdoptante(String dni) {
        return adoptantes.removeIf(p -> p instanceof Adoptante a && a.getDni().equalsIgnoreCase(dni));
    }

    // Coordina la adopción: llama al método de Adoptante y elimina el animal del refugio
    public String adoptarAnimal(String dni, int idAnimal) {
        Adoptante adoptante = buscarAdoptantePorDni(dni);
        if (adoptante == null) {
            return "No existe un adoptante con DNI: " + dni;
        }

        Animal animal = buscarAnimalPorID(idAnimal);
        if (animal == null) {
            return "No existe un animal con ID: " + idAnimal;
        }

        if (!(animal instanceof AnimalDomestico)) {
            return "Solo se pueden adoptar animales domésticos.";
        }

        adoptante.adoptarAnimal(animal); // marca el animal como adoptado y lo añade a su lista
        eliminarAnimal(idAnimal);        // lo quita de la lista general del refugio
        return "✅ " + adoptante.getNombre() + " ha adoptado a " + animal.getNombre();
    }

    // ============================================================
    // EMPLEADOS (comparten lista con adoptantes)
    // ============================================================
    public void añadirEmpleado(String nombre, String dni, String telefono,
            int idEmpleado, String tipoEmpleado, String tipoAnimalCargo) {

        adoptantes.add(new Empleado(nombre, dni, telefono, idEmpleado, tipoEmpleado, tipoAnimalCargo));
    }

    public Empleado buscarEmpleadoPorId(int id) {
        for (Persona p : adoptantes) {
            if (p instanceof Empleado e && e.getIdEmpleado() == id) {
                return e;
            }
        }
        return null;
    }

    public boolean eliminarEmpleado(int id) {
        return adoptantes.removeIf(p -> p instanceof Empleado e && e.getIdEmpleado() == id);
    }

    // ============================================================
    // DONACIONES
    // ============================================================
    public void registrarDonacion(Donacion d) {
        this.donaciones.add(d);
    }

    public void añadirDonacion(String donante, double cantidad, String tipo) {
        Donacion d = new Donacion(donante, cantidad, LocalDate.now(), tipo);
        donaciones.add(d);
    }

    public Donacion buscarDonacionPorId(int id) {
        if (id < 0 || id >= donaciones.size()) {
            return null;
        }
        return donaciones.get(id);
    }

    public boolean eliminarDonacion(int id) {
        if (id < 0 || id >= donaciones.size()) {
            return false;
        }
        donaciones.remove(id);
        return true;
    }

    public double calcularTotalDonaciones() {
        double total = 0.0;
        for (Donacion d : donaciones) {
            total += d.getTotalDonado();
        }
        return total;
    }

    // ============================================================
    // IMPORTAR CSV DESDE ARCHIVO SUELTO (GUI)
    // ============================================================
    public String impCSVArchivo(Path archivo) {
        try {
            if (!Files.exists(archivo)) {
                return "El archivo no existe: " + archivo;
            }

            var lineas = Files.readAllLines(archivo);
            if (lineas.isEmpty()) {
                return "El archivo CSV está vacío.";
            }

            String nombre = archivo.getFileName().toString().toLowerCase();

            if (nombre.contains("animal")) {
                return importarAnimales(lineas);
            }

            if (nombre.contains("adopt")) {
                return importarAdoptantes(lineas);
            }

            if (nombre.contains("don")) {
                return importarDonaciones(lineas);
            }

            return "No se reconoce el tipo de archivo CSV.";

        } catch (Exception e) {
            return "Error al importar archivo: " + e.getMessage();
        }
    }

    // ============================================================
    // IMPORTAR CSV DESDE CARPETA (CONSOLA)
    // ============================================================
    @Override
    public String impCSV(Path path) {
        try {
            animales.clear();
            donaciones.clear();
            adoptantes.clear();

            Path animalesPath = path.resolve("animales.csv");
            if (Files.exists(animalesPath)) {
                importarAnimales(Files.readAllLines(animalesPath));
            }

            Path donacionesPath = path.resolve("donaciones.csv");
            if (Files.exists(donacionesPath)) {
                importarDonaciones(Files.readAllLines(donacionesPath));
            }

            Path adoptantesPath = path.resolve("adoptantes.csv");
            if (Files.exists(adoptantesPath)) {
                importarAdoptantes(Files.readAllLines(adoptantesPath));
            }

            return "Importación completada desde carpeta: " + path;

        } catch (Exception e) {
            return "Error al importar CSV: " + e.getMessage();
        }
    }

    // ============================================================
    // MÉTODOS INTERNOS DE IMPORTACIÓN
    // ============================================================
    private String importarAnimales(java.util.List<String> lineas) {
        try {
            animales.clear();

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");

                int id = Integer.parseInt(p[0]);
                String nombre = p[1];
                String especie = p[2];
                int edad = Integer.parseInt(p[3]);
                double peso = Double.parseDouble(p[4]);
                String tipo = p[5];

                Animal a = null;

                if (tipo.equalsIgnoreCase("domestico")) {
                    boolean adoptado = Boolean.parseBoolean(p[6]);
                    boolean sociable = Boolean.parseBoolean(p[7]);
                    boolean vacunado = Boolean.parseBoolean(p[8]);

                    a = new AnimalDomestico(id, nombre, especie, edad, peso,
                            adoptado, sociable, vacunado);

                } else if (tipo.equalsIgnoreCase("salvaje")) {
                    String habitat = p[6];
                    boolean peligroso = Boolean.parseBoolean(p[7]);
                    boolean protegido = Boolean.parseBoolean(p[8]);
                    boolean liberado = Boolean.parseBoolean(p[9]);

                    a = new AnimalSalvaje(id, nombre, especie, edad, peso,
                            habitat, peligroso, protegido, liberado);
                }

                if (a != null) {
                    animales.add(a);
                }
            }

            return "Animales importados correctamente.";

        } catch (Exception e) {
            return "Error importando animales: " + e.getMessage();
        }
    }

    private String importarDonaciones(java.util.List<String> lineas) {
        try {
            donaciones.clear();

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");

                String donante = p[0];
                double cantidad = Double.parseDouble(p[1]);
                String tipo = p[2];
                LocalDate fecha = LocalDate.parse(p[3]);

                Donacion d = new Donacion(donante, cantidad, fecha, tipo);
                donaciones.add(d);
            }

            return "Donaciones importadas correctamente.";

        } catch (Exception e) {
            return "Error importando donaciones: " + e.getMessage();
        }
    }

    private String importarAdoptantes(java.util.List<String> lineas) {
        try {
            adoptantes.clear();

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",");

                String nombre = p[0];
                String dni = p[1];
                String telefono = p[2];

                Adoptante a = new Adoptante(nombre, dni, telefono);
                adoptantes.add(a);
            }

            return "Adoptantes importados correctamente.";

        } catch (Exception e) {
            return "Error importando adoptantes: " + e.getMessage();
        }
    }

    // ============================================================
    // EXPORTAR CSV
    // ============================================================
    @Override
    public String expCSV(Path path) {
        try {
            // Crear la carpeta si no existe (evita error al escribir los ficheros)
            java.nio.file.Files.createDirectories(path);

            // ================== ANIMALES ==================
            StringBuilder sbAnimales = new StringBuilder();
            sbAnimales.append("id,nombre,especie,edad,peso,tipo,extra1,extra2,extra3,extra4\n");

            for (Animal a : animales) {
                sbAnimales.append(a.getId()).append(",")
                        .append(a.getNombre()).append(",")
                        .append(a.getEspecie()).append(",")
                        .append(a.getEdad()).append(",")
                        .append(a.getPeso()).append(",");

                if (a instanceof AnimalDomestico ad) {
                    sbAnimales.append("domestico,")
                            .append(ad.isAdoptado()).append(",")
                            .append(ad.isSociable()).append(",")
                            .append(ad.isVacunado()).append(",")
                            .append("");
                } else if (a instanceof AnimalSalvaje as) {
                    sbAnimales.append("salvaje,")
                            .append(as.getHabitatOrigen()).append(",")
                            .append(as.isPeligroso()).append(",")
                            .append(as.isProtegido()).append(",")
                            .append(as.isLiberado());
                }

                sbAnimales.append("\n");
            }

            Files.writeString(path.resolve("animales.csv"), sbAnimales.toString());

            // ================== DONACIONES ==================
            StringBuilder sbDonaciones = new StringBuilder();
            sbDonaciones.append("donante,cantidad,tipo,fecha\n");
            for (Donacion d : donaciones) {
                sbDonaciones.append(d.toCSV()).append("\n");
            }
            Files.writeString(path.resolve("donaciones.csv"), sbDonaciones.toString());

            // ================== ADOPTANTES ==================
            StringBuilder sbAdoptantes = new StringBuilder();
            sbAdoptantes.append("nombre,dni,telefono\n");
            for (Persona p : adoptantes) {
                if (p instanceof Adoptante a) {
                    sbAdoptantes.append(a.getNombre()).append(",")
                            .append(a.getDni()).append(",")
                            .append(a.getTelefono()).append("\n");
                }
            }
            Files.writeString(path.resolve("adoptantes.csv"), sbAdoptantes.toString());

            return "Exportación completada en carpeta: " + path;

        } catch (Exception e) {
            return "Error al exportar CSV: " + e.getMessage();
        }
    }
}
