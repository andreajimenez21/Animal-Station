package com.aj.refugio.gui;

import com.aj.refugio.service.Refugio;

import javax.swing.*;
import java.awt.*;

// ============================================================
// HomePanel.java — PANTALLA DE INICIO (DASHBOARD)
// ============================================================
// Es lo primero que se ve al abrir la app.
// Muestra 4 tarjetas con el conteo actual de:
//   🐶 Animales | 👤 Adoptantes | 💰 Donaciones | 👷 Empleados
//
// ¿De dónde salen los números?
//   Del objeto Refugio que le pasa MainFrame.
//   Por ejemplo: refugio.animales.size() cuenta cuántos
//   animales hay en la lista en ese momento.
//
// NOTA: este panel NO tiene botones de acción.
//   Es solo de consulta visual. Para añadir datos,
//   hay que ir a cada sección del sidebar.
//
// RELACIÓN CON EL PROGRAMA:
//   HomePanel recibe → Refugio (service/Refugio.java)
//   Muestra datos de → refugio.animales (ArrayList<Animal>)
//                   → refugio.adoptantes (ArrayList<Persona>)
//                   → refugio.donaciones (ArrayList<Donacion>)
// ============================================================

public class HomePanel extends JPanel {

    private Refugio refugio;
    private JPanel cardsPanel; // referencia para poder refrescar

    // El constructor recibe el Refugio compartido de MainFrame
    public HomePanel(Refugio refugio) {
        this.refugio = refugio;
        setBackground(MainFrame.COLOR_SECONDARY);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36)); // márgenes internos

        add(buildHeader(), BorderLayout.NORTH);  // título arriba
        cardsPanel = buildCards();
        add(cardsPanel,  BorderLayout.CENTER); // tarjetas en el centro
        add(buildFooter(), BorderLayout.SOUTH); // botón refrescar abajo
    }

    // Título y subtítulo en la parte superior
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false); // transparente para ver el fondo gris
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Bienvenido al Refugio 🐾");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 30));

        // Muestra el nombre y dirección del refugio (viene del objeto Refugio)
        JLabel sub = new JLabel(refugio.getNombre() + "  ·  " + refugio.getDireccion());
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(120, 120, 120));

        p.add(title, BorderLayout.NORTH);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    // Barra inferior con botón de refresco manual
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        footer.setOpaque(false);

        JButton btnRefrescar = GuiUtils.secondaryButton("🔄 Refrescar datos");
        btnRefrescar.addActionListener(e -> refrescar());
        footer.add(btnRefrescar);
        return footer;
    }

    // Las 4 tarjetas de estadísticas en cuadrícula 2x2
    private JPanel buildCards() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16)); // 2 filas, 2 col, 16px de separación
        grid.setOpaque(false);

        // Solo se cuentan animales disponibles:
        // domésticos NO adoptados + salvajes NO liberados
        long animalesDisponibles = refugio.animales.stream().filter(a -> {
            if (a instanceof com.aj.refugio.model.AnimalDomestico ad) return !ad.isAdoptado();
            if (a instanceof com.aj.refugio.model.AnimalSalvaje as)  return !as.isLiberado();
            return true;
        }).count();

        grid.add(buildStatCard("🐶", "Animales",
            String.valueOf(animalesDisponibles),
            new Color(52, 199, 123)));

        grid.add(buildStatCard("👤", "Adoptantes",
            String.valueOf(
                // Filtramos solo los que son Adoptante (no Empleado, que también está en la lista)
                refugio.adoptantes.stream()
                    .filter(p -> p instanceof com.aj.refugio.model.Adoptante).count()
            ), new Color(88, 166, 255)));

        grid.add(buildStatCard("💰", "Donaciones",
            String.valueOf(refugio.donaciones.size()),
            new Color(255, 193, 7)));

        grid.add(buildStatCard("👷", "Empleados",
            String.valueOf(
                // Mismo truco: filtramos solo los Empleado de la lista de personas
                refugio.adoptantes.stream()
                    .filter(p -> p instanceof com.aj.refugio.model.Empleado).count()
            ), new Color(255, 107, 107)));

        return grid;
    }

    /**
     * Recalcula y redibuja las 4 tarjetas con los datos actuales del refugio.
     * Llamar después de cualquier operación que cambie los datos (adopción, etc.)
     */
    public void refrescar() {
        remove(cardsPanel);
        cardsPanel = buildCards();
        add(cardsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Construye una tarjeta individual con emoji grande, número en color y etiqueta
    private JPanel buildStatCard(String emoji, String label, String value, Color accent) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); // apila contenido verticalmente
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true), // borde redondeado
            BorderFactory.createEmptyBorder(20, 20, 20, 20)                    // relleno interno
        ));

        JLabel ico = new JLabel(emoji);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        ico.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel val = new JLabel(value);  // el número grande (ej: "5")
        val.setFont(new Font("Segoe UI", Font.BOLD, 34));
        val.setForeground(accent); // cada tarjeta tiene su color propio
        val.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label); // el texto de pie (ej: "Animales")
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(ico);
        card.add(Box.createVerticalStrut(8)); // espacio entre emoji y número
        card.add(val);
        card.add(lbl);
        return card;
    }
}
