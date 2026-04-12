package com.aj.refugio;

import com.aj.refugio.gui.MainFrame;

// ============================================================
// AppWin.java — PUNTO DE ENTRADA DE LA VENTANA
// ============================================================
// Para arrancar la app con ventana, ejecuta ESTE archivo.
// (Si quisieras la versión consola, ejecutarías App.java)
//
//   1. Avisa a Java: "abre la ventana en el hilo correcto"
//      SwingUtilities.invokeLater() es obligatorio en Swing,
//      si no, la ventana puede fallar o congelarse.
//   2. Crea un MainFrame (la ventana principal) y la muestra.
//
// RELACIÓN CON EL RESTO:
//   AppWin → crea MainFrame (gui/MainFrame.java)
//          → MainFrame crea el Refugio y todos los paneles
// ============================================================

public class AppWin {

    public static void main(String[] args) {
        // invokeLater = "hazlo cuando Swing esté listo"
        // Regla de oro: SIEMPRE arrancar ventanas Swing así.
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(); // crea la ventana
            frame.setVisible(true);            // la muestra en pantalla
        });
    }
}
