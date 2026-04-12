package com.aj.refugio.gui;

import com.aj.refugio.model.Empleado;
import com.aj.refugio.service.Refugio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Panel que muestra y gestiona los empleados del refugio.
// Permite buscar por ID de empleado, añadir uno nuevo y eliminar uno existente.
public class EmpleadosPanel extends JPanel {

    private Refugio refugio;
    private DefaultTableModel model;

    public EmpleadosPanel(Refugio refugio) {
        this.refugio = refugio;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_SECONDARY);

        // ─────────────────────────────
        // TÍTULO
        // ─────────────────────────────
        JLabel title = new JLabel("👷 Empleados", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // ─────────────────────────────
        // TABLA
        // ─────────────────────────────
        String[] columnas = {"ID", "Nombre", "DNI", "Teléfono", "Tipo Empleado", "Animales a Cargo"};
        model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);
        GuiUtils.styleTable(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();

        // ─────────────────────────────
        // BARRA INFERIOR (Buscar / Añadir / Eliminar)
        // ─────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(MainFrame.COLOR_SECONDARY);

        JTextField txtBuscar = new JTextField(8);
        JButton btnBuscar    = GuiUtils.secondaryButton("🔍 Buscar ID");
        JButton btnAñadir    = GuiUtils.primaryButton("➕ Añadir");
        JButton btnEliminar  = GuiUtils.dangerButton("🗑 Eliminar");

        topBar.add(new JLabel("ID:"));
        topBar.add(txtBuscar);
        topBar.add(btnBuscar);
        topBar.add(btnAñadir);
        topBar.add(btnEliminar);

        add(topBar, BorderLayout.SOUTH);

        // ─────────────────────────────
        // EVENTOS
        // ─────────────────────────────

        // Busca un empleado por su ID numérico y muestra sus datos en un popup
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText().trim());
                Empleado emp = refugio.buscarEmpleadoPorId(id);
                if (emp == null) {
                    JOptionPane.showMessageDialog(this, "No existe un empleado con ID " + id);
                } else {
                    JOptionPane.showMessageDialog(this, emp.toString());
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un ID numérico válido.");
            }
        });

        // Abre un formulario para introducir los datos del nuevo empleado
        btnAñadir.addActionListener(e -> añadirEmpleado());

        // Elimina el empleado cuyo ID está escrito en el campo de búsqueda
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText().trim());
                boolean eliminado = refugio.eliminarEmpleado(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Empleado eliminado.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "No existe un empleado con ese ID.");
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un ID numérico válido.");
            }
        });
    }

    // Recarga la tabla leyendo los empleados actuales de la lista del refugio
    private void cargarDatos() {
        model.setRowCount(0);
        for (var p : refugio.adoptantes) {
            if (p instanceof Empleado e) {
                model.addRow(new Object[]{
                        e.getIdEmpleado(), e.getNombre(), e.getDni(),
                        e.getTelefono(), e.getTipoEmpleado(), e.getTipoAnimalCargo()
                });
            }
        }
    }

    // Muestra un formulario para rellenar todos los campos del nuevo empleado
    private void añadirEmpleado() {
        JTextField fNombre      = new JTextField(14);
        JTextField fDni         = new JTextField(14);
        JTextField fTelefono    = new JTextField(14);
        JTextField fId          = new JTextField(14);
        JTextField fTipoEmp     = new JTextField(14);
        JTextField fTipoAnimal  = new JTextField(14);

        JPanel form = GuiUtils.buildForm(
                new String[]{
                        "Nombre", "DNI", "Teléfono",
                        "ID Empleado", "Tipo (admin/veterinario/cuidador)",
                        "Animales a cargo (doméstico/salvaje/ambos)"
                },
                new JComponent[]{fNombre, fDni, fTelefono, fId, fTipoEmp, fTipoAnimal}
        );

        int opcion = JOptionPane.showConfirmDialog(
                this, form, "Añadir Empleado", JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String nombre     = fNombre.getText().trim();
                String dni        = fDni.getText().trim();
                String telefono   = fTelefono.getText().trim();
                int idEmpleado    = Integer.parseInt(fId.getText().trim());
                String tipoEmp    = fTipoEmp.getText().trim();
                String tipoAnimal = fTipoAnimal.getText().trim();

                if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()
                        || tipoEmp.isEmpty() || tipoAnimal.isEmpty()) {
                    GuiUtils.showError(this, "Todos los campos son obligatorios.");
                    return;
                }

                refugio.añadirEmpleado(nombre, dni, telefono, idEmpleado, tipoEmp, tipoAnimal);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Empleado añadido correctamente.");

            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "El ID de empleado debe ser un número entero.");
            }
        }
    }
}
