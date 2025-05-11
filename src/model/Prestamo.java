package model;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private int idUsuario;
    private int idDocumento;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    //NUEVOS CAMPOS para mostrar nombres en la tabla
    private String nombreUsuario;
    private String nombreDocumento;

    // Constructor original
    public Prestamo(int id, int idUsuario, int idDocumento, LocalDate fechaPrestamo, boolean devuelto, LocalDate fechaDevolucion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idDocumento = idDocumento;
        this.fechaPrestamo = fechaPrestamo;
        this.devuelto = devuelto;
        this.fechaDevolucion = fechaDevolucion;
    }

    //Constructor nuevo con nombres
    // Getters y Setters
    public int getId() {
        return id;
    }


    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setId(int id) {
        this.id = id;
    }

    //NUEVOS Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", idDocumento=" + idDocumento +
                ", nombreDocumento='" + nombreDocumento + '\'' +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", devuelto=" + devuelto +
                '}';
    }
}