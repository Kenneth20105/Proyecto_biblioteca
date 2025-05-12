package gui;

import model.Prestamo;
import model.Usuario;
import persistence.DocumentoDAO;
import persistence.PrestamoDAO;
import persistence.UsuarioDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestorBiblioteca {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final DocumentoDAO documentoDAO;
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public GestorBiblioteca() throws SQLException {
        documentoDAO = new DocumentoDAO();
    }

    public List<Usuario> obtenerUsuarios() throws SQLException {
        return usuarioDAO.obtenerTodos();
    }

    public void modificarUsuario(Usuario usuario) throws SQLException {
        usuarioDAO.modificarUsuario(usuario);
    }

    public void eliminarUsuario(int id) throws SQLException {
        usuarioDAO.eliminarUsuario(id);
    }

    public List<String> obtenerDocumentosExtendidos() throws SQLException {
        return documentoDAO.obtenerDocumentosExtendidos();
    }

    public void agregarDocumentoCompleto(String titulo, String autor, int anio, String tipo, String campo1, String campo2) throws SQLException {
        int idDocumento = documentoDAO.agregarDocumentoBase(titulo, autor, anio, tipo);

        switch (tipo.toLowerCase()) {
            case "libro":
                documentoDAO.agregarLibro(idDocumento, campo1, Integer.parseInt(campo2));
                break;
            case "revista":
                documentoDAO.agregarRevista(idDocumento, Integer.parseInt(campo1), campo2);
                break;
            case "cd":
                documentoDAO.agregarCD(idDocumento, campo1, campo2);  // ahora campo2 es String (HH:MM:SS)
                break;
            case "dvd":
                documentoDAO.agregarDVD(idDocumento, campo1, campo2); // igual que CD
                break;
            case "tesis":
                documentoDAO.agregarTesis(idDocumento, campo1, campo2);
                break;
            case "pdf":
                documentoDAO.agregarPDF(idDocumento, campo1, Integer.parseInt(campo2));
                break;
            default:
                throw new SQLException("Tipo de documento no reconocido: " + tipo);
        }
    }

    public void prestarDocumento(int idUsuario, int idDocumento) throws SQLException {
        Usuario usuario = usuarioDAO.obtenerPorId(idUsuario);
        int diasPrestamo = usuario.getRol().equalsIgnoreCase("profesor") ? 15 : 7;

        Date fechaPrestamo = new Date(System.currentTimeMillis());
        Date fechaDevolucion = Date.valueOf(LocalDate.now().plusDays(diasPrestamo));

        prestamoDAO.prestarDocumento(idUsuario, idDocumento, fechaPrestamo, fechaDevolucion);
    }
    public void eliminarPrestamo(int idPrestamo) throws SQLException {
        prestamoDAO.eliminarPrestamo(idPrestamo);
    }
    public boolean existePrestamo(int id) throws SQLException {
        return prestamoDAO.existePrestamo(id);
    }

    public void devolverDocumento(int idPrestamo) throws SQLException {
        if (prestamoDAO.estaDevuelto(idPrestamo)) {
            throw new SQLException("Este préstamo ya fue devuelto.");
        }
        prestamoDAO.devolverDocumento(idPrestamo);
    }

    public List<Prestamo> obtenerPrestamos() throws SQLException {
        return prestamoDAO.obtenerTodos();
    }

    //Retorna el nombre del tipo de usuario (ej. "administrador")
    public String validarCredenciales(String username, String password) throws SQLException {
        Usuario usuario = usuarioDAO.validarCredenciales(username, password);
        return (usuario != null) ? usuario.getRol() : null;
    }
    public void agregarUsuarioCompleto(String nombre, String username, String password, String rol) throws SQLException {
        Usuario nuevo = new Usuario(nombre, username, password, rol);
        usuarioDAO.agregarUsuario(nuevo);
    }
    public boolean puedePrestarDocumento(int usuario_id) throws SQLException {
        Usuario usuario = usuarioDAO.obtenerPorId(usuario_id);
        int prestamosActivos = prestamoDAO.contarPrestamosActivos(usuario_id);

        if (usuario.getRol().equalsIgnoreCase("profesor")) {
            return prestamosActivos < 6;
        } else if (usuario.getRol().equalsIgnoreCase("alumno")) {
            return prestamosActivos < 3;
        } else {
            return true; // Administradores u otros roles no tienen restricción
        }
    }
    public List<String> buscarDocumentosPorFiltros(String titulo, String autor, String tipo) throws SQLException {
        return documentoDAO.buscarDocumentosPorFiltros(titulo, autor, tipo);
    }
    public Usuario obtenerUsuarioPorId(int id) throws SQLException {
        return usuarioDAO.obtenerPorId(id);
    }

}
