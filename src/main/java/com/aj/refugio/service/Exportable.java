package com.aj.refugio.service;

import java.nio.file.Path;

public interface Exportable {

    // Exporta TODO el refugio a CSV
    String expCSV(Path path);

    // Importa TODO el refugio desde CSV
    String impCSV(Path path);
}
