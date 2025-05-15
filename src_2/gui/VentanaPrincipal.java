package gui;

import model.Documento;
import model.Prestamo;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private GestorBiblioteca gestor;
    private Usuario usuario;
    private JTextArea areaSalida;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        try {
            gestor = new GestorBiblioteca();
        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        setTitle("Sistema Biblioteca Don Bosco - [" + usuario.getRol().toUpperCase() + "]");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout());

// Banner a la izquierda
        try {
            ImageIcon banner = new ImageIcon("banner.png");  // Imagen tipo "Biblioteca Amigos de Don Bosco"
            JLabel labelBanner = new JLabel(banner);
            panelSuperior.add(labelBanner, BorderLayout.WEST);
        } catch (Exception e) {
            JLabel labelFallback = new JLabel("📚 Biblioteca Amigos de Don Bosco");
            labelFallback.setFont(new Font("Arial", Font.BOLD, 16));
            panelSuperior.add(labelFallback, BorderLayout.WEST);
        }

// Botón usuario 
        JButton botonUsuario = new JButton("👤");
        botonUsuario.setFocusPainted(false);
        botonUsuario.setMargin(new Insets(4, 8, 4, 8));
        botonUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// Menú emergente
        JPopupMenu menuUsuario = new JPopupMenu();

        JMenuItem itemPrestar = new JMenuItem("Prestar");
        itemPrestar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String idDocumentoStr = JOptionPane.showInputDialog("ID del Documento:");
                    if (idDocumentoStr == null) return;

                    int idDocumento = Integer.parseInt(idDocumentoStr);

                    if (!gestor.puedePrestarDocumento(usuario)) {
                        mostrarError("No puedes realizar el préstamo: mora o límite alcanzado.");
                        return;
                    }

                    gestor.prestarDocumento(usuario.getId(), idDocumento);
                    mostrarMensaje("Préstamo registrado correctamente.");
                } catch (Exception ex) {
                    mostrarError("Error al prestar documento: " + ex.getMessage());
                }
            }
        });

        JMenuItem itemMora = new JMenuItem("Mi Mora");
        itemMora.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int mora = gestor.obtenerMoraDeUsuario(usuario.getId());
                    mostrarMensaje("Tu mora actual es: " + mora);
                } catch (Exception ex) {
                    mostrarError("Error al obtener la mora.");
                }
            }
        });

        JMenuItem itemCerrar = new JMenuItem("Cerrar Sesión");
        itemCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginWindow().setVisible(true);
            }
        });

        menuUsuario.add(itemPrestar);
        menuUsuario.add(itemMora);
        menuUsuario.add(itemCerrar);

// Mostrar el menú al hacer clic
        botonUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuUsuario.show(botonUsuario, 0, botonUsuario.getHeight());
            }
        });

// Alineación a la derecha
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecho.setOpaque(false);
        panelDerecho.add(botonUsuario);
        panelSuperior.add(panelDerecho, BorderLayout.EAST);

// Añadir panel completo al frame
        add(panelSuperior, BorderLayout.NORTH);

        // PANEL CENTRAL
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaSalida);
        add(scrollPane, BorderLayout.CENTER);

        // PANEL INFERIOR
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAgregarDocumento = new JButton("Agregar Documento");
        btnAgregarDocumento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarSeleccionTipo();
            }
        });

        JButton btnMostrarDocumentos = new JButton("Mostrar Documentos");
        btnMostrarDocumentos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDocumentos();
            }
        });

        JButton btnAgregarUsuario = new JButton("Agregar Usuario");
        btnAgregarUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField nombreField = new JTextField();
                JTextField carnetField = new JTextField();
                JTextField passwordField = new JTextField();
                String[] roles = {"administrador", "profesor", "alumno"};
                JComboBox<String> rolCombo = new JComboBox<>(roles);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Nombre:"));
                panel.add(nombreField);
                panel.add(new JLabel("Carnet:"));
                panel.add(carnetField);
                panel.add(new JLabel("Contraseña:"));
                panel.add(passwordField);
                panel.add(new JLabel("Rol:"));
                panel.add(rolCombo);

                int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Nuevo Usuario",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String nombre = nombreField.getText().trim();
                        String carnet = carnetField.getText().trim();
                        String password = passwordField.getText().trim();
                        String rol = (String) rolCombo.getSelectedItem();

                        if (nombre.isEmpty() || carnet.isEmpty() || password.isEmpty()) {
                            mostrarError("Todos los campos deben estar completos.");
                            return;
                        }

                        Usuario nuevo = new Usuario(nombre, carnet, password, rol);
                        gestor.agregarUsuario(nuevo);
                        mostrarMensaje("Usuario agregado correctamente.");
                    } catch (Exception ex) {
                        mostrarError("Error al agregar usuario: " + ex.getMessage());
                    }
                }
            }
        });

        JButton btnMostrarUsuarios = new JButton("Mostrar Usuarios");
        btnMostrarUsuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Usuario> usuarios = gestor.obtenerUsuarios();
                    areaSalida.setText("");
                    for (Usuario u : usuarios) {
                        areaSalida.append(u.toString() + "\n");
                    }
                } catch (SQLException ex) {
                    mostrarError("Error al obtener usuarios: " + ex.getMessage());
                }
            }
        });

        JButton btnDevolverDocumento = new JButton("Devolver Documento");
        btnDevolverDocumento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String idPrestamoStr = JOptionPane.showInputDialog("Ingrese el ID del préstamo a devolver:");
                    if (idPrestamoStr == null) return;
                    int idPrestamo = Integer.parseInt(idPrestamoStr);

                    gestor.devolverDocumento(idPrestamo);
                    mostrarMensaje("Documento devuelto correctamente.");
                } catch (Exception ex) {
                    mostrarError("Error al devolver documento: " + ex.getMessage());
                }
            }
        });

        JButton btnModificarUsuario = new JButton("Modificar Usuario");
        btnModificarUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carnet = JOptionPane.showInputDialog("Ingrese el carnet del usuario a modificar:");
                if (carnet == null || carnet.trim().isEmpty()) return;

                try {
                    Usuario usuario = gestor.obtenerUsuarioPorCarnet(carnet);
                    if (usuario == null) {
                        mostrarError("Usuario no encontrado con carnet: " + carnet);
                        return;
                    }

                    JTextField nombreField = new JTextField(usuario.getNombre());
                    JTextField passwordField = new JTextField(usuario.getPassword());
                    String[] roles = {"administrador", "profesor", "alumno"};
                    JComboBox<String> rolCombo = new JComboBox<>(roles);
                    rolCombo.setSelectedItem(usuario.getRol());

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Nombre:"));
                    panel.add(nombreField);
                    panel.add(new JLabel("Contraseña:"));
                    panel.add(passwordField);
                    panel.add(new JLabel("Rol:"));
                    panel.add(rolCombo);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Modificar Usuario",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        usuario.setNombre(nombreField.getText().trim());
                        usuario.setPassword(passwordField.getText().trim());
                        usuario.setRol((String) rolCombo.getSelectedItem());

                        gestor.modificarUsuario(usuario);
                        mostrarMensaje("Usuario modificado correctamente.");
                    }

                } catch (Exception ex) {
                    mostrarError("Error al modificar usuario: " + ex.getMessage());
                }
            }
        });

        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        btnEliminarUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carnet = JOptionPane.showInputDialog("Ingrese el carnet del usuario a eliminar:");
                if (carnet == null || carnet.trim().isEmpty()) return;

                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar al usuario con carnet: " + carnet + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Usuario usuario = gestor.obtenerUsuarioPorCarnet(carnet);
                        if (usuario == null) {
                            mostrarError("No se encontró usuario con el carnet: " + carnet);
                            return;
                        }
                        gestor.eliminarUsuario(usuario.getId());
                        mostrarMensaje("Usuario eliminado correctamente.");
                    } catch (Exception ex) {
                        mostrarError("Error al eliminar usuario: " + ex.getMessage());
                    }
                }
            }
        });

        JButton btnEliminarPrestamo = new JButton("Eliminar Préstamo");
        btnEliminarPrestamo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog("Ingrese el ID del préstamo a eliminar:");
                if (idStr == null || idStr.trim().isEmpty()) return;

                try {
                    int idPrestamo = Integer.parseInt(idStr);

                    int confirm = JOptionPane.showConfirmDialog(null,
                            "¿Seguro que deseas eliminar el préstamo con ID: " + idPrestamo + "?",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        gestor.eliminarPrestamo(idPrestamo);
                        mostrarMensaje("Préstamo eliminado correctamente.");
                    }

                } catch (NumberFormatException ex) {
                    mostrarError("ID inválido.");
                } catch (Exception ex) {
                    mostrarError("Error al eliminar préstamo: " + ex.getMessage());
                }
            }
        });

        // BOTONERA
        botonera.add(btnAgregarDocumento);
        botonera.add(btnMostrarDocumentos);
        botonera.add(btnAgregarUsuario);
        botonera.add(btnModificarUsuario);
        botonera.add(btnMostrarUsuarios);
        botonera.add(btnEliminarUsuario);
        botonera.add(btnDevolverDocumento);
        botonera.add(btnEliminarPrestamo);

        if (usuario.getRol().equalsIgnoreCase("administrador")) {
            JButton btnPrestamosActivos = new JButton("Préstamos Activos");
            btnPrestamosActivos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        List<Prestamo> prestamos = gestor.obtenerPrestamosActivos();
                        areaSalida.setText("");
                        for (Prestamo p : prestamos) {
                            areaSalida.append(p.toString() + "\n");
                        }
                    } catch (SQLException ex) {
                        mostrarError("Error al obtener préstamos activos: " + ex.getMessage());
                    }
                }
            });
            botonera.add(btnPrestamosActivos); 
        }

        add(botonera, BorderLayout.SOUTH); 
    } 

    

    private void mostrarSeleccionTipo() {
        String[] tipos = {"Libro", "Revista", "CD", "DVD", "PDF", "Tesis"};
        String tipoSeleccionado = (String) JOptionPane.showInputDialog(
                this, "Seleccione el tipo de documento:", "Nuevo Documento",
                JOptionPane.PLAIN_MESSAGE, null, tipos, tipos[0]);

        if (tipoSeleccionado == null) return;

        switch (tipoSeleccionado.toLowerCase()) {
            case "libro":
                new AgregarLibroDialog(this, gestor).setVisible(true);
                break;
            case "revista":
                new AgregarRevistaDialog(this, gestor).setVisible(true);
                break;
            case "cd":
                new AgregarCDDialog(this, gestor).setVisible(true);
                break;
            case "dvd":
                new AgregarDVDDialog(this, gestor).setVisible(true);
                break;
            case "pdf":
                new AgregarPDFDialog(this, gestor).setVisible(true);
                break;
            case "tesis":
                new AgregarTesisDialog(this, gestor).setVisible(true);
                break;
        }
    }

    private void mostrarDocumentos() {
        try {
            List<Documento> docs = gestor.obtenerDocumentos();
            areaSalida.setText("");
            for (Documento doc : docs) {
                areaSalida.append(doc.toString() + "\n");
            }
        } catch (SQLException ex) {
            mostrarError("Error al mostrar documentos: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
