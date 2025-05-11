package persistence;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {
    private final Connection connection;

    public DocumentoDAO() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca_donbosco", "root", "kenny3.01");
    }

    public int agregarDocumentoBase(String titulo, String autor, int anio, String tipo) throws SQLException {
        String sql = "INSERT INTO documentos (titulo, autor, año_publicacion, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setInt(3, anio);
            stmt.setString(4, tipo);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del documento.");
                }
            }
        }
    }

    public void agregarLibro(int id, String editorial, int paginas) throws SQLException {
        String sql = "INSERT INTO libros (id, editorial, numero_paginas) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, editorial);
            stmt.setInt(3, paginas);
            stmt.executeUpdate();
        }
    }

    public void agregarRevista(int id, int numero, String mes) throws SQLException {
        String sql = "INSERT INTO revistas (id, numero, mes) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, numero);
            stmt.setString(3, mes);
            stmt.executeUpdate();
        }
    }

    public void agregarCD(int id, String genero, String duracion) throws SQLException {
        String sql = "INSERT INTO cds (id, genero, duracion_min) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, genero);
            stmt.setString(3, duracion); // ya viene como "02:30:05"
            stmt.executeUpdate();
        }
    }

    public void agregarDVD(int id, String director, String duracion) throws SQLException {
        String sql = "INSERT INTO dvds (id, director, duracion_min) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, director);
            stmt.setString(3, duracion);
            stmt.executeUpdate();
        }
    }

    public void agregarTesis(int id, String universidad, String autorEstudiante) throws SQLException {
        String sql = "INSERT INTO tesis (id, universidad, autor_estudiante) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, universidad);
            stmt.setString(3, autorEstudiante);
            stmt.executeUpdate();
        }
    }

    public void agregarPDF(int id, String tema, int paginas) throws SQLException {
        String sql = "INSERT INTO pdfs (id, tema, numero_paginas) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, tema);
            stmt.setInt(3, paginas);
            stmt.executeUpdate();
        }
    }

    public List<String> obtenerDocumentosExtendidos() throws SQLException {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT d.id, d.titulo, d.autor, d.tipo, " +
                "CASE " +
                "    WHEN d.tipo = 'libro' THEN l.editorial " +
                "    WHEN d.tipo = 'revista' THEN r.numero " +
                "    WHEN d.tipo = 'cd' THEN c.genero " +
                "    WHEN d.tipo = 'dvd' THEN dv.director " +
                "    WHEN d.tipo = 'tesis' THEN t.universidad " +
                "    WHEN d.tipo = 'pdf' THEN p.tema " +
                "END AS campo1, " +
                "CASE " +
                "    WHEN d.tipo = 'libro' THEN l.numero_paginas " +
                "    WHEN d.tipo = 'revista' THEN r.mes " +
                "    WHEN d.tipo = 'cd' THEN c.duracion_min " +   // ya no se castea
                "    WHEN d.tipo = 'dvd' THEN dv.duracion_min " + // ya no se castea
                "    WHEN d.tipo = 'tesis' THEN t.autor_estudiante " +
                "    WHEN d.tipo = 'pdf' THEN p.numero_paginas " +
                "END AS campo2 " +
                "FROM documentos d " +
                "LEFT JOIN libros l ON d.id = l.id " +
                "LEFT JOIN revistas r ON d.id = r.id " +
                "LEFT JOIN cds c ON d.id = c.id " +
                "LEFT JOIN dvds dv ON d.id = dv.id " +
                "LEFT JOIN tesis t ON d.id = t.id " +
                "LEFT JOIN pdfs p ON d.id = p.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fila = "ID: " + rs.getInt("id") + " | " +
                        "Título: " + rs.getString("titulo") + " | " +
                        "Autor: " + rs.getString("autor") + " | " +
                        "Tipo: " + rs.getString("tipo") + " | " +
                        "Campo1: " + rs.getString("campo1") + " | " +
                        "Campo2: " + rs.getString("campo2");
                lista.add(fila);
            }
        }

        return lista;
    }
    public List<String> buscarDocumentosPorFiltros(String titulo, String autor, String tipo) throws SQLException {
        List<String> resultados = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT d.id, d.titulo, d.autor, d.tipo, " +
                        "CASE " +
                        "    WHEN d.tipo = 'libro' THEN l.editorial " +
                        "    WHEN d.tipo = 'revista' THEN r.numero " +
                        "    WHEN d.tipo = 'cd' THEN c.genero " +
                        "    WHEN d.tipo = 'dvd' THEN dv.director " +
                        "    WHEN d.tipo = 'tesis' THEN t.universidad " +
                        "    WHEN d.tipo = 'pdf' THEN p.tema " +
                        "END AS campo1, " +
                        "CASE " +
                        "    WHEN d.tipo = 'libro' THEN CAST(l.numero_paginas AS CHAR) " +
                        "    WHEN d.tipo = 'revista' THEN r.mes " +
                        "    WHEN d.tipo = 'cd' THEN CAST(c.duracion_min AS CHAR) " +
                        "    WHEN d.tipo = 'dvd' THEN CAST(dv.duracion_min AS CHAR) " +
                        "    WHEN d.tipo = 'tesis' THEN t.autor_estudiante " +
                        "    WHEN d.tipo = 'pdf' THEN CAST(p.numero_paginas AS CHAR) " +
                        "END AS campo2 " +
                        "FROM documentos d " +
                        "LEFT JOIN libros l ON d.id = l.id " +
                        "LEFT JOIN revistas r ON d.id = r.id " +
                        "LEFT JOIN cds c ON d.id = c.id " +
                        "LEFT JOIN dvds dv ON d.id = dv.id " +
                        "LEFT JOIN tesis t ON d.id = t.id " +
                        "LEFT JOIN pdfs p ON d.id = p.id " +
                        "WHERE 1=1 "
        );

        if (!titulo.isEmpty()) {
            sql.append("AND d.titulo LIKE ? ");
        }
        if (!autor.isEmpty()) {
            sql.append("AND d.autor LIKE ? ");
        }
        if (!tipo.isEmpty()) {
            sql.append("AND d.tipo = ? ");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (!titulo.isEmpty()) {
                stmt.setString(index++, "%" + titulo + "%");
            }
            if (!autor.isEmpty()) {
                stmt.setString(index++, "%" + autor + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String fila = "ID: " + rs.getInt("id") + " | " +
                            "Título: " + rs.getString("titulo") + " | " +
                            "Autor: " + rs.getString("autor") + " | " +
                            "Tipo: " + rs.getString("tipo") + " | " +
                            "Campo1: " + rs.getString("campo1") + " | " +
                            "Campo2: " + rs.getString("campo2");
                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }
}
