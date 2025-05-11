package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class LoginWindow extends JFrame {
    private final GestorBiblioteca gestor;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public LoginWindow() throws SQLException {
        gestor = new GestorBiblioteca();
        setTitle("Login - Biblioteca Don Bosco");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirUI();
    }

    private void construirUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Logo o título
        JLabel lblTitulo = new JLabel("Biblioteca Don Bosco");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);

        // Añadir imagen como logo (debajo del título)
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\kfjva\\IdeaProjects\\Proyecto_Fase_1\\Don-Bosco imagen.png"); // Cambia esta ruta
        // Redimensionar la imagen si es necesario
        Image image = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(image);
        JLabel lblLogo = new JLabel(logoIcon);
        gbc.gridx = 0;
        gbc.gridy = 1;  // Posición debajo del título
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblLogo, gbc);

        // Campos de formulario
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 2;  // Ahora es la posición 2 (debajo del logo)
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        panel.add(txtContrasena, gbc);

        // Botón de login
        JButton btnLogin = new JButton("Iniciar Sesión");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> autenticarUsuario());

        // Ajustar tamaño de la ventana para acomodar el logo
        setSize(350, 350);  // Aumenté la altura para el logo

        add(panel);
    }

    private void autenticarUsuario() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String rol = gestor.validarCredenciales(usuario, contrasena);

            if (rol != null) {
                dispose(); // Cierra la ventana de login
                new VentanaPrincipal(rol).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        }
    }
}