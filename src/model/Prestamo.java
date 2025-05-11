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
    public Prestamo(int id, int idUsuario, String nombreUsuario, int idDocumento, String nombreDocumento,
                    LocalDate fechaPrestamo, boolean devuelto, LocalDate fechaDevolucion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idDocumento = idDocumento;
        this.nombreDocumento = nombreDocumento;
        this.fechaPrestamo = fechaPrestamo;
        this.devuelto = devuelto;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public int getIdUsuario() {
        return idUsuario;
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

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
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