
package persistence;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {
    private final Connection connection;

    public DocumentoDAO() throws SQLException {
        this.connection = Conexion.getConnection();
    }

    public int agregarDocumentoBase(Documento doc) throws SQLException {
        String sql = "INSERT INTO documentos (titulo, autor, anio_publicacion, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doc.getTitulo());
            stmt.setString(2, doc.getAutor());
            stmt.setInt(3, doc.getAnioPublicacion());
            stmt.setString(4, doc.getTipo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        }
    }

    public void agregarDocumento(Documento doc) throws SQLException {
        int id = agregarDocumentoBase(doc);
        String tipo = doc.getTipo();

        switch (tipo) {
            case "libro":
                Libro libro = (Libro) doc;
                String sqlLibro = "INSERT INTO libros (id, isbn, editorial, numero_paginas) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlLibro)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, libro.getIsbn());
                    stmt.setString(3, libro.getEditorial());
                    stmt.setInt(4, libro.getNumeroPaginas());
                    stmt.executeUpdate();
                }
                break;
            case "revista":
                Revista revista = (Revista) doc;
                String sqlRevista = "INSERT INTO revistas (id, numero, mes, categoria, editorial) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlRevista)) {
                    stmt.setInt(1, id);
                    stmt.setInt(2, revista.getNumero());
                    stmt.setString(3, revista.getMes());
                    stmt.setString(4, revista.getCategoria());
                    stmt.setString(5, revista.getEditorial());
                    stmt.executeUpdate();
                }
                break;
            case "cd":
                CD cd = (CD) doc;
                String sqlCD = "INSERT INTO cds (id, genero, duracion, artista) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlCD)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, cd.getGenero());
                    stmt.setString(3, cd.getDuracion());
                    stmt.setString(4, cd.getArtista());
                    stmt.executeUpdate();
                }
                break;
            case "dvd":
                DVD dvd = (DVD) doc;
                String sqlDVD = "INSERT INTO dvds (id, director, duracion, productora) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlDVD)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, dvd.getDirector());
                    stmt.setString(3, dvd.getDuracion());
                    stmt.setString(4, dvd.getProductora());
                    stmt.executeUpdate();
                }
                break;
            case "pdf":
                PDF pdf = (PDF) doc;
                String sqlPDF = "INSERT INTO pdfs (id, tema, numero_paginas, autor_original) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlPDF)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, pdf.getTema());
                    stmt.setInt(3, pdf.getNumeroPaginas());
                    stmt.setString(4, pdf.getAutorOriginal());
                    stmt.executeUpdate();
                }
                break;
            case "tesis":
                Tesis tesis = (Tesis) doc;
                String sqlTesis = "INSERT INTO tesis (id, carrera, universidad, asesor_academico) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlTesis)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, tesis.getCarrera());
                    stmt.setString(3, tesis.getUniversidad());
                    stmt.setString(4, tesis.getAsesorAcademico());
                    stmt.executeUpdate();
                }
                break;
        }
    }
    public void eliminarDocumento(int id) throws SQLException {
        String sql = "DELETE FROM documentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Documento> obtenerTodos() throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = "SELECT * FROM documentos";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int anio = rs.getInt("anio_publicacion");
                String tipo = rs.getString("tipo");

                documentos.add(new Documento(id, titulo, autor, anio, tipo)); // Por ahora cargamos genéricos
            }
        }
        return documentos;
    }

    public List<String[]> obtenerDocumentosParaVista() throws SQLException {
        List<String[]> documentos = new ArrayList<>();
        String sql =
                "SELECT d.titulo, d.autor, d.anio_publicacion, d.tipo,\n" +
                        "       r.categoria,\n" +
                        "       l.editorial, r.numero, r.mes,\n" +
                        "       COALESCE(cd.duracion, dv.duracion) AS duracion\n" +
                        "FROM documentos d\n" +
                        "LEFT JOIN libros l ON d.id = l.id\n" +
                        "LEFT JOIN revistas r ON d.id = r.id\n" +
                        "LEFT JOIN cds cd ON d.id = cd.id\n" +
                        "LEFT JOIN dvds dv ON d.id = dv.id\n" +
                        "LEFT JOIN pdfs p ON d.id = p.id\n" +
                        "LEFT JOIN tesis t ON d.id = t.id";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[9];
                fila[0] = rs.getString("titulo");
                fila[1] = rs.getString("autor");
                fila[2] = String.valueOf(rs.getInt("anio_publicacion"));
                fila[3] = rs.getString("tipo");
                fila[4] = rs.getString("categoria");
                fila[5] = rs.getString("editorial");
                fila[6] = rs.getString("numero");
                fila[7] = rs.getString("mes");
                fila[8] = rs.getString("duracion");
                documentos.add(fila);
            }
        }

        return documentos;
    }
}
