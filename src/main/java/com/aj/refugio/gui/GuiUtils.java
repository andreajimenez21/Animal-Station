package com.aj.refugio.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

// ============================================================
// GuiUtils.java — CAJA DE HERRAMIENTAS VISUAL
// ============================================================
// Este archivo NO muestra nada por sí solo.
// Es una colección de métodos estáticos (= no hace falta crear
// un objeto, se llaman directamente con GuiUtils.nombreMetodo())
// que usan todos los paneles para no repetir código.
//
// ¿Qué problemas resuelve?
//   Sin GuiUtils, cada panel tendría que escribir 20 líneas
//   para hacer un botón verde. Con GuiUtils es una línea:
//     JButton btn = GuiUtils.primaryButton("➕ Añadir");
//
// MÉTODOS DISPONIBLES:
//   primaryButton(texto)   → botón verde principal
//   secondaryButton(texto) → botón gris secundario
//   dangerButton(texto)    → botón rojo para eliminar
//   styleTable(tabla)      → aplica el estilo visual a una JTable
//   buildForm(labels,fields)→ crea un formulario etiqueta-campo
//   showError(parent, msg) → muestra un popup de error
// ============================================================

public class GuiUtils {

    // ── Botones ────────────────────────────────────────────────────────────

    // Botón verde: para acciones principales como "Añadir" o "Registrar"
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setBackground(MainFrame.COLOR_PRIMARY); // verde definido en MainFrame
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);   // quita el cuadradito feo al hacer clic
        btn.setBorderPainted(false);  // sin borde
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        // Efecto hover: verde más oscuro al pasar el ratón
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(28, 115, 72));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(MainFrame.COLOR_PRIMARY);
            }
        });
        return btn;
    }

    // Botón gris: para acciones secundarias como "Buscar" o "Ver detalle"
    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(new Color(40, 40, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(210, 210, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(230, 230, 230));
            }
        });
        return btn;
    }

    // Botón rojo: solo para acciones destructivas como "Eliminar"
    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btn.setBackground(MainFrame.COLOR_DANGER); // rojo definido en MainFrame
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(180, 30, 45));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(MainFrame.COLOR_DANGER);
            }
        });
        return btn;
    }

    // ── Tablas ─────────────────────────────────────────────────────────────

    // Aplica el mismo estilo visual a cualquier JTable que se le pase.
    // Se usa en AnimalesPanel, AdoptantesPanel, DonacionesPanel y EmpleadosPanel.
    // Sin esto, las tablas saldrían con el aspecto feo por defecto de Java.
    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);         // altura de cada fila en píxeles
        table.setSelectionBackground(new Color(52, 199, 123, 80)); // verde semitransparente al seleccionar
        table.setSelectionForeground(new Color(20, 20, 20));
        table.setGridColor(new Color(230, 230, 230));  // color de las líneas de la cuadrícula
        table.setBackground(Color.WHITE);
        table.setShowHorizontalLines(true);  // líneas horizontales sí
        table.setShowVerticalLines(false);   // líneas verticales no (más limpio)
        table.setIntercellSpacing(new Dimension(0, 1));

        // Estilo de la cabecera (la fila de títulos de columna)
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(new Color(60, 60, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        header.setReorderingAllowed(false); // impide que el usuario mueva columnas
    }

    // ── Formularios ────────────────────────────────────────────────────────

    // Genera un panel con pares "Etiqueta : Campo de texto"
    // Recibe un array de textos (labels) y un array de campos (fields)
    // y los coloca en una cuadrícula de 2 columnas.
    //
    // Ejemplo de uso en AnimalesPanel:
    //   JTextField fNombre = new JTextField(14);
    //   JTextField fEdad   = new JTextField(5);
    //   JPanel form = GuiUtils.buildForm(
    //       new String[]{"Nombre", "Edad"},
    //       new JComponent[]{fNombre, fEdad}
    //   );
    public static JPanel buildForm(String[] labels, JComponent[] fields) {
        // GridLayout(filas, 2 columnas) → etiqueta | campo
        JPanel form = new JPanel(new GridLayout(labels.length, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            form.add(lbl);       // columna izquierda: etiqueta
            form.add(fields[i]); // columna derecha: campo de entrada
        }
        return form;
    }

    // ── Mensajes ───────────────────────────────────────────────────────────

    // Muestra un popup de error con el mensaje dado.
    // El parámetro 'parent' es el panel desde el que se llama,
    // para que el popup aparezca centrado sobre él.
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
