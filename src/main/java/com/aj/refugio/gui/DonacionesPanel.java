package com.aj.refugio.gui;

import com.aj.refugio.model.Donacion;
import com.aj.refugio.service.Refugio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Panel que muestra y gestiona las donaciones registradas en el refugio.
// Permite buscar por posición (índice), añadir una nueva donación y eliminar una existente.
public class DonacionesPanel extends JPanel {

    private Refugio refugio;
    private DefaultTableModel model;

    public DonacionesPanel(Refugio refugio) {
        this.refugio = refugio;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_SECONDARY);

        // ─────────────────────────────
        // TÍTULO
        // ─────────────────────────────
        JLabel title = new JLabel("💰 Donaciones", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // ─────────────────────────────
        // TABLA
        // ─────────────────────────────
        String[] columnas = {"#", "Donante", "Cantidad (€)", "Tipo", "Fecha"};
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
        JButton btnBuscar    = GuiUtils.secondaryButton("🔍 Buscar #");
        JButton btnAñadir    = GuiUtils.primaryButton("➕ Añadir");
        JButton btnEliminar  = GuiUtils.dangerButton("🗑 Eliminar");

        topBar.add(new JLabel("Nº:"));
        topBar.add(txtBuscar);
        topBar.add(btnBuscar);
        topBar.add(btnAñadir);
        topBar.add(btnEliminar);

        add(topBar, BorderLayout.SOUTH);

        // ─────────────────────────────
        // EVENTOS
        // ─────────────────────────────

        // Busca la donación por su número de fila y muestra sus datos en un popup
        btnBuscar.addActionListener(e -> {
            try {
                int idx = Integer.parseInt(txtBuscar.getText().trim());
                Donacion d = refugio.buscarDonacionPorId(idx);
                if (d == null) {
                    JOptionPane.showMessageDialog(this, "No existe ninguna donación con el número " + idx);
                } else {
                    JOptionPane.showMessageDialog(this, d.toString());
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un número válido.");
            }
        });

        // Abre un formulario para registrar una nueva donación
        btnAñadir.addActionListener(e -> añadirDonacion());

        // Elimina la donación cuyo número está en el campo de búsqueda
        btnEliminar.addActionListener(e -> {
            try {
                int idx = Integer.parseInt(txtBuscar.getText().trim());
                boolean eliminado = refugio.eliminarDonacion(idx);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Donación eliminada.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "No existe ninguna donación con ese número.");
                }
            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "Introduce un número válido.");
            }
        });
    }

    // Recarga la tabla mostrando el número de posición de cada donación
    private void cargarDatos() {
        model.setRowCount(0);
        for (int i = 0; i < refugio.donaciones.size(); i++) {
            Donacion d = refugio.donaciones.get(i);
            model.addRow(new Object[]{
                    i, d.getDonante(), d.getCantidad(), d.getTipo(), d.getFecha()
            });
        }
    }

    // Muestra un formulario para introducir los datos de la nueva donación
    private void añadirDonacion() {
        JTextField fDonante  = new JTextField(14);
        JTextField fCantidad = new JTextField(14);
        JTextField fTipo     = new JTextField(14);

        JPanel form = GuiUtils.buildForm(
                new String[]{"Donante", "Cantidad (€)", "Tipo (dinero/comida/medicinas)"},
                new JComponent[]{fDonante, fCantidad, fTipo}
        );

        int opcion = JOptionPane.showConfirmDialog(
                this, form, "Añadir Donación", JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String donante  = fDonante.getText().trim();
                double cantidad = Double.parseDouble(fCantidad.getText().trim());
                String tipo     = fTipo.getText().trim();

                if (donante.isEmpty() || tipo.isEmpty()) {
                    GuiUtils.showError(this, "Todos los campos son obligatorios.");
                    return;
                }

                refugio.añadirDonacion(donante, cantidad, tipo);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Donación registrada correctamente.");

            } catch (NumberFormatException ex) {
                GuiUtils.showError(this, "La cantidad debe ser un número válido (ej: 50.0).");
            }
        }
    }
}
