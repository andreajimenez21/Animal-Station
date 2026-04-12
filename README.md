# 🐾 AnimalStation — Refugio de Animales

Proyecto integrado de **Programación DAW 1.º** (RA5 · RA6 · RA7) — Curso 2024-2025.

**Equipo:** [Nombre Alumno 1] · [Nombre Alumno 2]

---

## Descripción

AnimalStation es una aplicación Java para la gestión de un refugio de animales.
Permite registrar animales domésticos y salvajes, gestionar adoptantes, empleados y donaciones,
y persistir todos los datos en ficheros CSV.

La app tiene dos modos de uso:
- **Consola** — menú interactivo por terminal (`App.java`)
- **Ventana** — interfaz gráfica con Swing (`AppWin.java`)

---

## Requisitos previos

- Java 17 o superior
- Maven 3.8 o superior

---

## Compilar

Desde la raíz del proyecto (donde está el `pom.xml`):

```bash
mvn package
```

Esto genera el archivo `target/animalstation-1.0-SNAPSHOT.jar`.

---

## Ejecutar

### Versión consola

```bash
java -cp target/animalstation-1.0-SNAPSHOT.jar com.aj.refugio.App
```

### Versión gráfica (Swing)

```bash
java -cp target/animalstation-1.0-SNAPSHOT.jar com.aj.refugio.AppWin
```

---

## Importar / Exportar datos CSV

Los ficheros CSV de ejemplo están en la carpeta `importacion/`:

| Fichero            | Contenido                  |
|--------------------|----------------------------|
| `animales.csv`     | Animales del refugio       |
| `adoptantes.csv`   | Adoptantes registrados     |
| `donaciones.csv`   | Donaciones recibidas       |

### Desde la consola
- Opción `6` → Importar CSV (indica la ruta de la carpeta)
- Opción `7` → Exportar CSV (indica la ruta de la carpeta de destino)

### Desde la GUI
- Botón **📥 Importar CSV** → selecciona el fichero CSV
- Botón **📤 Exportar CSV** → selecciona la carpeta donde guardar los tres ficheros

---

## Estructura del proyecto

```
AnimalStation/
├── pom.xml
├── README.md
├── uml/
│   └── diagrama.png
├── importacion/
│   ├── animales.csv
│   ├── adoptantes.csv
│   └── donaciones.csv
├── exportacion/
└── src/main/java/com/aj/refugio/
    ├── App.java                  ← entrada consola
    ├── AppWin.java               ← entrada GUI
    ├── model/
    │   ├── Animal.java           ← superclase abstracta
    │   ├── AnimalDomestico.java
    │   ├── AnimalSalvaje.java
    │   ├── Persona.java
    │   ├── Adoptante.java
    │   ├── Empleado.java
    │   └── Donacion.java
    ├── service/
    │   ├── Refugio.java          ← lógica principal y persistencia CSV
    │   └── Exportable.java       ← interfaz de importación/exportación
    └── gui/
        ├── MainFrame.java
        ├── HomePanel.java
        ├── AnimalesPanel.java
        ├── AdoptantesPanel.java
        ├── DonacionesPanel.java
        ├── EmpleadosPanel.java
        └── GuiUtils.java
```

---

## Jerarquía de clases principal

```
Animal  (abstracta)
├── AnimalDomestico   → atributos: sociable, vacunado, adoptado
└── AnimalSalvaje     → atributos: habitatOrigen, peligroso, protegido, liberado

Persona  (abstracta)
├── Adoptante         → puede adoptar animales domésticos
└── Empleado          → tiene tipo y animales a cargo
```

---

## Tecnologías utilizadas

- Java 17 (SE puro, sin dependencias externas)
- Swing (GUI construida a mano)
- Maven (solo como herramienta de build)
- CSV y JSON manual (sin librerías)
