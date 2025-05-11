import gui.LoginWindow;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginWindow login = null;
            try {
                login = new LoginWindow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            login.setVisible(true);
        });
    }
}