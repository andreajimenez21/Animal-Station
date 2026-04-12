package com.aj.refugio.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.aj.refugio.service.Refugio;

public class MainFrame extends JFrame {

    // 🎨 Colores globales
    public static final Color COLOR_PRIMARY = new Color(34, 139, 87);
    public static final Color COLOR_SECONDARY = new Color(245, 245, 245);
    public static final Color COLOR_SIDEBAR = new Color(30, 30, 30);
    public static final Color COLOR_ACCENT = new Color(52, 199, 123);
    public static final Color COLOR_TEXT = new Color(220, 220, 220);
    public static final Color COLOR_DANGER = new Color(220, 53, 69);
    public static final Color COLOR_WHITE = Color.WHITE;

    private Refugio refugio;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {

        refugio = new Refugio("Libertad", "Calle Desengaño 21");

        setTitle("🐾 AnimalStation — Refugio " + refugio.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    // ───────────────────────────────────────────────
    // SIDEBAR
    // ───────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BorderLayout());

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(COLOR_SIDEBAR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 20, 0));

        JLabel icon = new JLabel("🐾", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("AnimalStation", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Refugio " + refugio.getNombre(), SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(new Color(150, 150, 150));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(icon);
        logoPanel.add(Box.createVerticalStrut(6));
        logoPanel.add(title);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(subtitle);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Navegación
        JPanel nav = new JPanel();
        nav.setBackground(COLOR_SIDEBAR);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        String[][] items = {
            {"🏠", "Inicio", "home"},
            {"🐶", "Animales", "animales"},
            {"👤", "Adoptantes", "adoptantes"},
            {"💰", "Donaciones", "donaciones"},
            {"👷", "Empleados", "empleados"}
        };

        for (String[] item : items) {
            nav.add(buildNavButton(item[0], item[1], item[2]));
            nav.add(Box.createVerticalStrut(6));
        }

        sidebar.add(nav, BorderLayout.CENTER);

        // ───────────────────────────────────────────────
        // BOTONES IMPORTAR / EXPORTAR
        // ───────────────────────────────────────────────
        JPanel actions = new JPanel();
        actions.setBackground(COLOR_SIDEBAR);
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JButton btnImportar = new JButton("📥 Importar CSV");
        btnImportar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnImportar.setForeground(COLOR_TEXT);
        btnImportar.setBackground(new Color(45, 45, 45));
        btnImportar.setFocusPainted(false);
        btnImportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImportar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImportar.addActionListener(e -> importarCSV());
        actions.add(btnImportar);
        actions.add(Box.createVerticalStrut(8));

        JButton btnExportar = new JButton("📤 Exportar CSV");
        btnExportar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnExportar.setForeground(COLOR_TEXT);
        btnExportar.setBackground(new Color(45, 45, 45));
        btnExportar.setFocusPainted(false);
        btnExportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExportar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExportar.addActionListener(e -> exportarCSV());
        actions.add(btnExportar);

        sidebar.add(actions, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton buildNavButton(String emoji, String label, String card) {
        JButton btn = new JButton(emoji + "  " + label);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addActionListener(e -> cardLayout.show(contentPanel, card));
        return btn;
    }

    // ───────────────────────────────────────────────
    // CONTENIDO CENTRAL
    // ───────────────────────────────────────────────
    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(COLOR_SECONDARY);

        HomePanel homePanel = new HomePanel(refugio);
        contentPanel.add(homePanel, "home");
        contentPanel.add(new AnimalesPanel(refugio, homePanel), "animales");
        contentPanel.add(new AdoptantesPanel(refugio), "adoptantes");
        contentPanel.add(new DonacionesPanel(refugio), "donaciones");
        contentPanel.add(new EmpleadosPanel(refugio), "empleados");

        cardLayout.show(contentPanel, "home");
        return contentPanel;
    }

    // ───────────────────────────────────────────────
    // IMPORTAR CSV
    // ───────────────────────────────────────────────
    public void importarCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona un archivo CSV");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV", "csv"));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            String mensaje = refugio.impCSVArchivo(archivo.toPath());

            JOptionPane.showMessageDialog(this, mensaje);

            //  Recargar paneles después de importar
            recargarPaneles();
        }
    }

    // ───────────────────────────────────────────────
    // EXPORTAR CSV
    // ───────────────────────────────────────────────
    public void exportarCSV() {

        // expCSV necesita una CARPETA: genera animales.csv, donaciones.csv y adoptantes.csv dentro
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona la carpeta donde guardar los CSV");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File carpeta = chooser.getSelectedFile();
            String mensaje = refugio.expCSV(carpeta.toPath());
            JOptionPane.showMessageDialog(this, mensaje);
        }
    }

    private void recargarPaneles() {
        contentPanel.removeAll();

        HomePanel homePanel = new HomePanel(refugio);
        contentPanel.add(homePanel, "home");
        contentPanel.add(new AnimalesPanel(refugio, homePanel), "animales");
        contentPanel.add(new AdoptantesPanel(refugio), "adoptantes");
        contentPanel.add(new DonacionesPanel(refugio), "donaciones");
        contentPanel.add(new EmpleadosPanel(refugio), "empleados");

        contentPanel.revalidate();
        contentPanel.repaint();

        cardLayout.show(contentPanel, "home");
    }

}
