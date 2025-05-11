package model;

public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String password;
    private String rol;

    // Constructor completo con ID (usado al validar credenciales o cargar usuarios)
    public Usuario(int id, String nombre, String username, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Constructor sin ID (usado para agregar nuevos usuarios)
    public Usuario(String nombre, String username, String password, String rol) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}