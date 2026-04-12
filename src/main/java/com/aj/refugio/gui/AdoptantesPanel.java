package com.aj.refugio.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.aj.refugio.model.Adoptante;
import com.aj.refugio.model.Persona;
import com.aj.refugio.service.Refugio;

// Panel que muestra y gestiona la lista de adoptantes del refugio.
// Permite buscar por DNI, añadir un nuevo adoptante, eliminar uno existente y gestionar adopciones.
public class AdoptantesPanel extends JPanel {

    private Refugio refugio;
    private DefaultTableModel model;

    public AdoptantesPanel(Refugio refugio) {
        this.refugio = refugio;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_SECONDARY);

        // ─────────────────────────────
        // TÍTULO
        // ─────────────────────────────
        JLabel title = new JLabel("👤 Adoptantes", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // ─────────────────────────────
        // TABLA
        // ─────────────────────────────
        String[] columnas = {"Nombre", "DNI", "Teléfono", "Animales adoptados"};
        model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);
        GuiUtils.styleTable(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();

        // ─────────────────────────────
        // BARRA INFERIOR (Buscar / Añadir / Eliminar / Adoptar)
        // ─────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(MainFrame.COLOR_SECONDARY);

        JTextField txtBuscar = new JTextField(12);
        JButton btnBuscar   = GuiUtils.secondaryButton("🔍 Buscar DNI");
        JButton btnAñadir   = GuiUtils.primaryButton("➕ Añadir");
        JButton btnEliminar = GuiUtils.dangerButton("🗑 Eliminar");
        JButton btnAdoptar  = GuiUtils.primaryButton("🐾 Adoptar Animal");

        topBar.add(new JLabel("DNI:"));
        topBar.add(txtBuscar);
        topBar.add(btnBuscar);
        topBar.add(btnAñadir);
        topBar.add(btnEliminar);
        topBar.add(btnAdoptar);

        add(topBar, BorderLayout.SOUTH);

        // ─────────────────────────────
        // EVENTOS
        // ─────────────────────────────

        // Busca un adoptante por su DNI y muestra sus datos en un popup
        btnBuscar.addActionListener(e -> {
            String dni = txtBuscar.getText().trim();
            if (dni.isEmpty()) {
                GuiUtils.showError(this, "Introduce un DNI para buscar.");
                return;
            }
            Adoptante a = refugio.buscarAdoptantePorDni(dni);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "No existe un adoptante con DNI: " + dni);
            } else {
                JOptionPane.showMessageDialog(this, a.toString());
            }
        });

        // Abre un formulario para introducir los datos del nuevo adoptante
        btnAñadir.addActionListener(e -> añadirAdoptante());

        // Elimina el adoptante cuyo DNI está escrito en el campo de búsqueda
        btnEliminar.addActionListener(e -> {
            String dni = txtBuscar.getText().trim();
            if (dni.isEmpty()) {
                GuiUtils.showError(this, "Introduce el DNI del adoptante a eliminar.");
                return;
            }
            boolean eliminado = refugio.eliminarAdoptante(dni);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Adoptante eliminado.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "No existe un adoptante con ese DNI.");
            }
        });

        // Permite al adoptante (buscado por DNI) adoptar un animal doméstico disponible
        btnAdoptar.addActionListener(e -> {
            String dni = txtBuscar.getText().trim();
            if (dni.isEmpty()) {
                GuiUtils.showError(this, "Introduce primero el DNI del adoptante.");
                return;
            }

            // Pedimos el ID del animal directamente
            String input = JOptionPane.showInputDialog(this, "Introduce el ID del animal doméstico a adoptar:");
            if (input == null) return;

            try {
                int idAnimal = Integer.parseInt(input.trim());
                String resultado = refugio.adoptarAnimal(dni, idAnimal);
                JOptionPane.showMessageDialog(this, resultado);
                cargarDatos();
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "El ID debe ser un número entero.");
            }
        });

    } // ← fin del constructor

    // Recarga la tabla leyendo la lista actualizada del refugio
    private void cargarDatos() {
        model.setRowCount(0);
        for (Persona p : refugio.adoptantes) {
            if (p instanceof Adoptante a) {
                model.addRow(new Object[]{
                        a.getNombre(), a.getDni(), a.getTelefono(),
                        a.getAnimalesAdoptados().size()
                });
            }
        }
    }

    // Muestra un formulario para rellenar los datos del nuevo adoptante
    private void añadirAdoptante() {
        JTextField fNombre   = new JTextField(14);
        JTextField fDni      = new JTextField(14);
        JTextField fTelefono = new JTextField(14);

        JPanel form = GuiUtils.buildForm(
                new String[]{"Nombre", "DNI", "Teléfono"},
                new JComponent[]{fNombre, fDni, fTelefono}
        );

        int opcion = JOptionPane.showConfirmDialog(
                this, form, "Añadir Adoptante", JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String nombre   = fNombre.getText().trim();
            String dni      = fDni.getText().trim();
            String telefono = fTelefono.getText().trim();

            if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
                GuiUtils.showError(this, "Todos los campos son obligatorios.");
                return;
            }

            refugio.añadirAdoptante(nombre, dni, telefono);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Adoptante añadido correctamente.");
        }
    }
}