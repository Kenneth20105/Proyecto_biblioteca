import java.sql.*;
public class TestConexion {
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:jdbc:mysql://localhost:3306/biblioteca_donbosco_v2", //Asegurense de tener la base de datos ya en su MYSQL
                "root", "kenny3.01" //Aqui ponen su contraseña de usuario en MYSQL
        );
        System.out.println("¡Conexión exitosa!");
        c.close();
    }
}
