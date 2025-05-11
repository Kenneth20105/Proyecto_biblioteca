package gui;

import model.Prestamo;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private GestorBiblioteca gestor;
    private String rolUsuario;
    private JTextArea areaSalida;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    public VentanaPrincipal(String rolUsuario) {
        this.rolUsuario = rolUsuario;
        try {
            gestor = new GestorBiblioteca();
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        setTitle("Sistema Biblioteca Don Bosco - [" + rolUsuario.toUpperCase() + "]");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Banner superior
        ImageIcon banner = new ImageIcon("C:\\Users\\kfjva\\IdeaProjects\\Proyecto_Fase_1\\src\\banner biblioteca.png");
        JLabel bannerLabel = new JLabel(banner);
        add(bannerLabel, BorderLayout.NORTH);

        // Panel de botones en columna
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelMenu.setBackground(Color.WHITE);
        panelMenu.setOpaque(true);

        // Botones
        JButton btnUsuarios = new JButton("Mostrar Usuarios");
        JButton btnDocumentos = new JButton("Mostrar Documentos");
        JButton btnPrestamos = new JButton("Préstamos Activos");
        JButton btnAgregarUsuario = new JButton("Agregar Usuario");
        JButton btnAgregarDocumento = new JButton("Agregar Documento");
        JButton btnModificarUsuario = new JButton("Modificar Usuario");
        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        JButton btnPrestarDocumento = new JButton("Prestar Documento");
        JButton btnDevolverDocumento = new JButton("Devolver Documento");
        JButton btnEliminarPrestamo = new JButton("Eliminar Préstamo");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        // Mostrar botones según el rol
        List<JButton> botonesVisibles = new ArrayList<>();
        if (!rolUsuario.equalsIgnoreCase("alumno")) botonesVisibles.add(btnUsuarios);
        botonesVisibles.add(btnDocumentos);
        botonesVisibles.add(btnPrestamos);
        botonesVisibles.add(btnPrestarDocumento);
        botonesVisibles.add(btnDevolverDocumento);
        botonesVisibles.add(btnCerrarSesion);
        if (rolUsuario.equalsIgnoreCase("administrador") || rolUsuario.equalsIgnoreCase("maestro")) {
            botonesVisibles.add(btnAgregarUsuario);
            botonesVisibles.add(btnAgregarDocumento);
        }
        if (rolUsuario.equalsIgnoreCase("administrador")) {
            botonesVisibles.add(btnModificarUsuario);
            botonesVisibles.add(btnEliminarUsuario);
        }
        if (rolUsuario.equalsIgnoreCase("administrador") || rolUsuario.equalsIgnoreCase("profesor")) {
            botonesVisibles.add(btnEliminarPrestamo);
        }

        for (JButton boton : botonesVisibles) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(200, 35));
            boton.setHorizontalAlignment(SwingConstants.CENTER);
            panelMenu.add(boton);
            panelMenu.add(Box.createVerticalStrut(8));
        }

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(panelMenu);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaSalida);

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.add(panelCentro, BorderLayout.NORTH);
        panelContenido.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(panelContenido, "menu");
        add(panelPrincipal, BorderLayout.CENTER);
        cardLayout.show(panelPrincipal, "menu");

        // Eventos
        btnUsuarios.addActionListener(e -> mostrarUsuarios());
        btnDocumentos.addActionListener(e -> mostrarDocumentos());
        btnPrestamos.addActionListener(e -> mostrarPrestamos());
        btnAgregarUsuario.addActionListener(e -> agregarUsuario());
        btnAgregarDocumento.addActionListener(e -> agregarDocumento());
        btnPrestarDocumento.addActionListener(e -> prestarDocumento());
        btnDevolverDocumento.addActionListener(e -> devolverDocumento());
        btnModificarUsuario.addActionListener(e -> modificarUsuario());
        btnEliminarUsuario.addActionListener(e -> eliminarUsuario());
        btnEliminarPrestamo.addActionListener(e -> eliminarPrestamo());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarUsuarios() {
        try {
            List<Usuario> usuarios = gestor.obtenerUsuarios();
            String[] columnas = {"ID", "Nombre", "Username", "Rol"};
            Object[][] datos = new Object[usuarios.size()][columnas.length];

            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                datos[i][0] = u.getId();
                datos[i][1] = u.getNombre();
                datos[i][2] = u.getUsername();
                datos[i][3] = u.getRol();
            }

            JTable tabla = new JTable(datos, columnas);
            JScrollPane scroll = new JScrollPane(tabla);

            JButton btnVolver = new JButton("Volver al menú");
            btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "menu"));

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(scroll, BorderLayout.CENTER);
            panelTabla.add(btnVolver, BorderLayout.SOUTH);

            panelPrincipal.add(panelTabla, "usuarios");
            cardLayout.show(panelPrincipal, "usuarios");

        } catch (SQLException e) {
            mostrarError("Error al obtener usuarios: " + e.getMessage());
        }
    }

    private void mostrarDocumentos() {
        try {
            List<String> documentos = gestor.obtenerDocumentosExtendidos();
            if (documentos.isEmpty()) {
                mostrarError("No hay documentos registrados.");
                return;
            }

            JTabbedPane pestañas = new JTabbedPane();

            List<String> libros = new ArrayList<>();
            List<String> revistas = new ArrayList<>();
            List<String> cds = new ArrayList<>();
            List<String> dvds = new ArrayList<>();
            List<String> tesis = new ArrayList<>();
            List<String> pdfs = new ArrayList<>();

            for (String doc : documentos) {
                if (doc.contains("Tipo: libro")) libros.add(doc);
                else if (doc.contains("Tipo: revista")) revistas.add(doc);
                else if (doc.contains("Tipo: cd")) cds.add(doc);
                else if (doc.contains("Tipo: dvd")) dvds.add(doc);
                else if (doc.contains("Tipo: tesis")) tesis.add(doc);
                else if (doc.contains("Tipo: pdf")) pdfs.add(doc);
            }

            if (!libros.isEmpty()) pestañas.add("Libros", crearPanelDocumento(libros, new String[]{"ID", "Título", "Autor", "Tipo", "Editorial", "Páginas"}));
            if (!revistas.isEmpty()) pestañas.add("Revistas", crearPanelDocumento(revistas, new String[]{"ID", "Título", "Autor", "Tipo", "Número", "Mes"}));
            if (!cds.isEmpty()) pestañas.add("CDs", crearPanelDocumento(cds, new String[]{"ID", "Título", "Autor", "Tipo", "Género", "Duración (min)"}));
            if (!dvds.isEmpty()) pestañas.add("DVDs", crearPanelDocumento(dvds, new String[]{"ID", "Título", "Autor", "Tipo", "Director", "Duración (min)"}));
            if (!tesis.isEmpty()) pestañas.add("Tesis", crearPanelDocumento(tesis, new String[]{"ID", "Título", "Autor", "Tipo", "Universidad", "Estudiante"}));
            if (!pdfs.isEmpty()) pestañas.add("PDFs", crearPanelDocumento(pdfs, new String[]{"ID", "Título", "Autor", "Tipo", "Tema", "Páginas"}));

            JButton btnVolver = new JButton("Volver al menú");
            btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "menu"));

            JPanel panelFinal = new JPanel(new BorderLayout());
            panelFinal.add(pestañas, BorderLayout.CENTER);
            panelFinal.add(btnVolver, BorderLayout.SOUTH);

            panelPrincipal.add(panelFinal, "documentos");
            cardLayout.show(panelPrincipal, "documentos");

        } catch (SQLException e) {
            mostrarError("Error al obtener documentos: " + e.getMessage());
        }
    }

    private JPanel crearPanelDocumento(List<String> lista, String[] columnas) {
        Object[][] datos = prepararDatos(lista, columnas);
        JTable tabla = new JTable(datos, columnas);
        JScrollPane scroll = new JScrollPane(tabla);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private Object[][] prepararDatos(List<String> lista, String[] columnas) {
        Object[][] datos = new Object[lista.size()][columnas.length];
        for (int i = 0; i < lista.size(); i++) {
            String[] partes = lista.get(i).split(" \\| ");
            for (int j = 0; j < columnas.length; j++) {
                String[] claveValor = partes[j].split(":", 2); // Split solo en el primer ":"
                datos[i][j] = claveValor.length > 1 ? claveValor[1].trim() : "";
            }
        }
        return datos;
    }

    private void mostrarPrestamos() {
        try {
            List<Prestamo> prestamos = gestor.obtenerPrestamos();

            String[] columnas = {"ID", "Usuario", "Documento", "Fecha Préstamo", "Fecha Devolución", "Devuelto", "Días de Mora", "Multa ($)"};
            Object[][] datos = new Object[prestamos.size()][columnas.length];
            double tarifaPorDia = 0.50;

            for (int i = 0; i < prestamos.size(); i++) {
                Prestamo p = prestamos.get(i);
                String fechaDev = (p.getFechaDevolucion() != null) ? p.getFechaDevolucion().toString() : "-";
                String devuelto = p.isDevuelto() ? "Sí" : "No";

                long diasMora = 0;
                double multa = 0.0;

                if (!p.isDevuelto() && p.getFechaDevolucion() != null && p.getFechaDevolucion().isBefore(LocalDate.now())) {
                    diasMora = java.time.temporal.ChronoUnit.DAYS.between(p.getFechaDevolucion(), LocalDate.now());
                    multa = diasMora * tarifaPorDia;
                }

                datos[i][0] = p.getId();
                datos[i][1] = p.getNombreUsuario();
                datos[i][2] = p.getNombreDocumento();
                datos[i][3] = p.getFechaPrestamo().toString();
                datos[i][4] = fechaDev;
                datos[i][5] = devuelto;
                datos[i][6] = diasMora > 0 ? diasMora : "-";
                datos[i][7] = diasMora > 0 ? String.format("%.2f", multa) : "-";
            }

            JTable tabla = new JTable(datos, columnas);
            JScrollPane scroll = new JScrollPane(tabla);

            JButton btnVolver = new JButton("Volver al menú");
            btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "menu"));

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(scroll, BorderLayout.CENTER);
            panelTabla.add(btnVolver, BorderLayout.SOUTH);

            panelPrincipal.add(panelTabla, "prestamos");
            cardLayout.show(panelPrincipal, "prestamos");

        } catch (SQLException e) {
            mostrarError("Error al obtener préstamos: " + e.getMessage());
        }
    }

    private void agregarUsuario() {
        JDialog dialog = new JDialog(this, "Agregar Usuario", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(this);

        JTextField campoNombre = new JTextField();
        JTextField campoUsername = new JTextField();
        JPasswordField campoPassword = new JPasswordField();
        JTextField campoRol = new JTextField();

        dialog.add(new JLabel("Nombre:"));
        dialog.add(campoNombre);
        dialog.add(new JLabel("Username:"));
        dialog.add(campoUsername);
        dialog.add(new JLabel("Contraseña:"));
        dialog.add(campoPassword);
        dialog.add(new JLabel("Rol:"));
        dialog.add(campoRol);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        dialog.add(btnGuardar);
        dialog.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String username = campoUsername.getText().trim();
            String password = new String(campoPassword.getPassword()).trim();
            String rol = campoRol.getText().trim();

            if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || rol.isEmpty()) {
                mostrarError("Todos los campos son obligatorios.");
                return;
            }

            try {
                gestor.agregarUsuarioCompleto(nombre, username, password, rol);
                JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.");
                dialog.dispose();
            } catch (SQLException ex) {
                mostrarError("Error al agregar usuario: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void modificarUsuario() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del usuario a modificar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr.trim());
            Usuario usuario = gestor.obtenerUsuarioPorId(id);
            if (usuario == null) {
                mostrarError("Usuario no encontrado.");
                return;
            }

            JTextField campoNombre = new JTextField(usuario.getNombre());
            JTextField campoUsername = new JTextField(usuario.getUsername());
            JPasswordField campoPassword = new JPasswordField(usuario.getPassword());
            JTextField campoRol = new JTextField(usuario.getRol());

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Nombre:"));
            panel.add(campoNombre);
            panel.add(new JLabel("Username:"));
            panel.add(campoUsername);
            panel.add(new JLabel("Contraseña:"));
            panel.add(campoPassword);
            panel.add(new JLabel("Rol:"));
            panel.add(campoRol);

            int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Usuario",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                usuario.setNombre(campoNombre.getText());
                usuario.setUsername(campoUsername.getText());
                usuario.setPassword(new String(campoPassword.getPassword()));
                usuario.setRol(campoRol.getText());

                gestor.modificarUsuario(usuario);
                JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
            }

        } catch (Exception e) {
            mostrarError("Error al modificar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del usuario a eliminar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr.trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea eliminar al usuario con ID " + id + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                gestor.eliminarUsuario(id);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            }

        } catch (Exception e) {
            mostrarError("Error al eliminar usuario: " + e.getMessage());
        }
    }

    private void agregarDocumento() {
        JDialog dialog = new JDialog(this, "Agregar Documento", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(this);

        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();
        JTextField campoAnio = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Libro", "Revista", "CD", "DVD", "Tesis", "PDF"});

        dialog.add(new JLabel("Título:"));
        dialog.add(campoTitulo);
        dialog.add(new JLabel("Autor:"));
        dialog.add(campoAutor);
        dialog.add(new JLabel("Año:"));
        dialog.add(campoAnio);
        dialog.add(new JLabel("Tipo:"));
        dialog.add(comboTipo);

        JButton btnSiguiente = new JButton("Siguiente");
        JButton btnCancelar = new JButton("Cancelar");
        dialog.add(btnSiguiente);
        dialog.add(btnCancelar);

        btnSiguiente.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            String autor = campoAutor.getText().trim();
            String anio = campoAnio.getText().trim();
            String tipo = comboTipo.getSelectedItem().toString().trim();

            if (titulo.isEmpty() || autor.isEmpty() || anio.isEmpty()) {
                mostrarError("Todos los campos generales son obligatorios.");
                return;
            }

            dialog.dispose();
            mostrarFormularioEspecifico(titulo, autor, anio, tipo);
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void mostrarFormularioEspecifico(String titulo, String autor, String anio, String tipo) {
        JDialog dialog = new JDialog(this, "Detalles de " + tipo, true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField campo1 = new JTextField();
        JTextField campo2 = new JTextField();

        switch (tipo.toLowerCase()) {
            case "libro":
                dialog.add(new JLabel("Editorial:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Páginas:"));
                dialog.add(campo2);
                break;
            case "revista":
                dialog.add(new JLabel("Número:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Mes:"));
                dialog.add(campo2);
                break;
            case "cd":
                dialog.add(new JLabel("Género:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Duración (hh:mm:ss):"));
                dialog.add(campo2);
                break;
            case "dvd":
                dialog.add(new JLabel("Director:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Duración (hh:mm:ss):"));
                dialog.add(campo2);
                break;
            case "tesis":
                dialog.add(new JLabel("Universidad:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Estudiante:"));
                dialog.add(campo2);
                break;
            case "pdf":
                dialog.add(new JLabel("Tema:"));
                dialog.add(campo1);
                dialog.add(new JLabel("Páginas:"));
                dialog.add(campo2);
                break;
            default:
                mostrarError("Tipo no reconocido.");
                return;
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        dialog.add(btnGuardar);
        dialog.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            if ((tipo.equalsIgnoreCase("cd") || tipo.equalsIgnoreCase("dvd")) &&
                    !campo2.getText().trim().matches("^\\d{2}:\\d{2}:\\d{2}$")) {
                mostrarError("Formato de duración inválido. Usa hh:mm:ss (ej. 02:30:05)");
                return;
            }
            try {
                gestor.agregarDocumentoCompleto(titulo, autor, Integer.parseInt(anio), tipo, campo1.getText().trim(), campo2.getText().trim());
                JOptionPane.showMessageDialog(this, tipo + " agregado correctamente.");
                dialog.dispose();
                mostrarDocumentos();
            } catch (Exception ex) {
                mostrarError("Error al guardar: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void prestarDocumento() {
        JDialog dialog = new JDialog(this, "Prestar Documento", true);
        dialog.setLayout(new GridLayout(3, 2));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JTextField campoIdUsuario = new JTextField();
        JTextField campoIdDocumento = new JTextField();

        dialog.add(new JLabel("ID Usuario:"));
        dialog.add(campoIdUsuario);
        dialog.add(new JLabel("ID Documento:"));
        dialog.add(campoIdDocumento);

        JButton btnAceptar = new JButton("Prestar");
        JButton btnCancelar = new JButton("Cancelar");
        dialog.add(btnAceptar);
        dialog.add(btnCancelar);

        btnAceptar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(campoIdUsuario.getText().trim());
                int idDocumento = Integer.parseInt(campoIdDocumento.getText().trim());

                if (!gestor.puedePrestarDocumento(idUsuario)) {
                    mostrarError("El usuario ha alcanzado el límite de préstamos permitidos.");
                    return;
                }

                gestor.prestarDocumento(idUsuario, idDocumento);
                JOptionPane.showMessageDialog(this, "Documento prestado correctamente.");
                dialog.dispose();
                mostrarPrestamos();

            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void devolverDocumento() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del préstamo a devolver:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr.trim());
            gestor.devolverDocumento(id);
            JOptionPane.showMessageDialog(this, "Documento devuelto correctamente.");
        } catch (Exception e) {
            mostrarError("Error al devolver documento: " + e.getMessage());
        }
    }

    private void eliminarPrestamo() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del préstamo a eliminar:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr.trim());
            if (!gestor.existePrestamo(id)) {
                mostrarError("No existe préstamo con ese ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el préstamo con ID " + id + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                gestor.eliminarPrestamo(id);
                JOptionPane.showMessageDialog(this, "Préstamo eliminado correctamente.");
            }

        } catch (Exception e) {
            mostrarError("Error al eliminar préstamo: " + e.getMessage());
        }
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            try {
                new LoginWindow().setVisible(true);
            } catch (SQLException e) {
                mostrarError("No se pudo volver al login: " + e.getMessage());
            }
        }
    }
}