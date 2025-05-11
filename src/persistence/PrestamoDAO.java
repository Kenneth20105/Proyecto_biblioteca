package persistence;

import model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private final Connection conn;

    public PrestamoDAO() throws SQLException {
        this.conn = Conexion.getConnection();
    }

    public void prestarDocumento(int idUsuario, int idDocumento, Date fechaPrestamo, Date fechaDevolucion) throws SQLException {
        String sql = "INSERT INTO prestamos (usuario_id, documento_id, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, false)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idDocumento);
            stmt.setDate(3, fechaPrestamo);
            stmt.setDate(4, fechaDevolucion);
            stmt.executeUpdate();
        }
    }
    public void eliminarPrestamo(int idPrestamo) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
        }
    }
    public boolean existePrestamo(int id) throws SQLException {
        String sql = "SELECT 1 FROM prestamos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void devolverDocumento(int idPrestamo) throws SQLException {
        String sql = "UPDATE prestamos SET devuelto = true, fecha_devolucion = CURRENT_DATE WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
        }
    }
    public boolean estaDevuelto(int idPrestamo) throws SQLException {
        String sql = "SELECT devuelto FROM prestamos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("devuelto");
            } else {
                throw new SQLException("El préstamo con ID " + idPrestamo + " no existe.");
            }
        }
    }

    public List<Prestamo> obtenerTodos() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.usuario_id, u.nombre AS nombre_usuario, " +
                "p.documento_id, d.titulo AS nombre_documento, " +
                "p.fecha_prestamo, p.devuelto, p.fecha_devolucion " +
                "FROM prestamos p " +
                "JOIN usuarios u ON p.usuario_id = u.id " +
                "JOIN documentos d ON p.documento_id = d.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Prestamo p = new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getInt("documento_id"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getBoolean("devuelto"),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null
                );
                // Seteamos los nuevos campos agregados
                p.setNombreUsuario(rs.getString("nombre_usuario"));
                p.setNombreDocumento(rs.getString("nombre_documento"));

                lista.add(p);
            }
        }

        return lista;
    }
    public int contarPrestamosActivos(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE usuario_id = ? AND devuelto = FALSE";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}