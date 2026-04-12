package com.aj.refugio.gui;

import com.aj.refugio.model.Animal;
import com.aj.refugio.service.Refugio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Panel que muestra y gestiona los animales del refugio.
// Permite buscar por ID, añadir un nuevo animal y eliminar uno existente.
public class AnimalesPanel extends JPanel {

    private Refugio refugio;
    private JTable tabla;
    private DefaultTableModel model;
    private HomePanel homePanel; // para refrescar el dashboard al cambiar datos

    public AnimalesPanel(Refugio refugio, HomePanel homePanel) {
        this.refugio = refugio;
        this.homePanel = homePanel;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_SECONDARY);

        // ─────────────────────────────
        // TÍTULO
        // ─────────────────────────────
        JLabel title = new JLabel("🐶 Animales del Refugio", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // ─────────────────────────────
        // TABLA
        // ─────────────────────────────
        String[] columnas = {"ID", "Nombre", "Especie", "Edad", "Peso", "Tipo"};
        model = new DefaultTableModel(columnas, 0);
        tabla = new JTable(model);
        GuiUtils.styleTable(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();

        // ─────────────────────────────
        // BARRA INFERIOR (Buscar / Añadir / Eliminar)
        // ─────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(MainFrame.COLOR_SECONDARY);

        JTextField txtBuscar = new JTextField(10);
        JButton btnBuscar    = GuiUtils.secondaryButton("🔍 Buscar ID");
        JButton btnAñadir    = GuiUtils.primaryButton("➕ Añadir");
        JButton btnEliminar  = GuiUtils.dangerButton("🗑 Eliminar");
        JButton btnRefrescar = GuiUtils.secondaryButton("🔄 Refrescar");

        topBar.add(new JLabel("ID:"));
        topBar.add(txtBuscar);
        topBar.add(btnBuscar);
        topBar.add(btnAñadir);
        topBar.add(btnEliminar);
        topBar.add(btnRefrescar);

        add(topBar, BorderLayout.SOUTH);

        // ─────────────────────────────
        // EVENTOS
        // ─────────────────────────────

        // Busca un animal por su ID y muestra todos sus datos en un popup
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText().trim());
                Animal a = refugio.buscarAnimalPorID(id);
                if (a == null) {
                    JOptionPane.showMessageDialog(this, "No existe un animal con ID " + id);
                } else {
                    JOptionPane.showMessageDialog(this, a.toString());
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un ID numérico válido.");
            }
        });

        // Abre el diálogo para rellenar los datos del nuevo animal
        btnAñadir.addActionListener(e -> añadirAnimal());

        // Refresca la tabla aplicando el filtro (sin adoptados ni liberados)
        btnRefrescar.addActionListener(e -> {
            cargarDatos();
            homePanel.refrescar(); // sincroniza también el dashboard
            JOptionPane.showMessageDialog(this, "Lista actualizada.");
        });

        // Elimina el animal cuyo ID está escrito en el campo de búsqueda
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText().trim());
                boolean eliminado = refugio.eliminarAnimal(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Animal eliminado.");
                    cargarDatos();
                    homePanel.refrescar(); // actualiza el contador del dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "No existe un animal con ese ID.");
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un ID numérico válido.");
            }
        });
    }

    // Borra la tabla y la rellena solo con animales disponibles:
    // - Domésticos NO adoptados
    // - Salvajes NO liberados
    private void cargarDatos() {
        model.setRowCount(0);
        for (Animal a : refugio.animales) {
            boolean mostrar = true;
            if (a instanceof com.aj.refugio.model.AnimalDomestico ad && ad.isAdoptado()) {
                mostrar = false; // doméstico ya adoptado: no se muestra
            }
            if (a instanceof com.aj.refugio.model.AnimalSalvaje as && as.isLiberado()) {
                mostrar = false; // salvaje ya liberado: no se muestra
            }
            if (mostrar) {
                model.addRow(new Object[]{
                        a.getId(), a.getNombre(), a.getEspecie(),
                        a.getEdad(), a.getPeso(), a.getTipo()
                });
            }
        }
    }

    // Muestra un formulario con los campos básicos para añadir un animal
    private void añadirAnimal() {
        JTextField fNombre  = new JTextField(14);
        JTextField fEspecie = new JTextField(14);
        JTextField fEdad    = new JTextField(6);
        JTextField fPeso    = new JTextField(6);
        JTextField fTipo    = new JTextField(14);

        JPanel form = GuiUtils.buildForm(
                new String[]{"Nombre", "Especie", "Edad", "Peso (kg)", "Tipo (domestico/salvaje)"},
                new JComponent[]{fNombre, fEspecie, fEdad, fPeso, fTipo}
        );

        int opcion = JOptionPane.showConfirmDialog(
                this, form, "Añadir Animal", JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String nombre  = fNombre.getText().trim();
                String especie = fEspecie.getText().trim();
                int edad       = Integer.parseInt(fEdad.getText().trim());
                double peso    = Double.parseDouble(fPeso.getText().trim());
                String tipo    = fTipo.getText().trim();

                if (nombre.isEmpty() || especie.isEmpty() || tipo.isEmpty()) {
                    GuiUtils.showError(this, "Nombre, especie y tipo son obligatorios.");
                    return;
                }

                // Refugio genera el ID automáticamente y crea el animal correcto según el tipo
                refugio.añadirAnimal(nombre, especie, edad, peso, tipo);
                cargarDatos();
                homePanel.refrescar(); // actualiza el contador del dashboard
                JOptionPane.showMessageDialog(this, "Animal añadido correctamente.");

            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Edad debe ser entero y Peso un número decimal (ej: 12.5).");
            }
        }
    }
}
