package model;

public class Documento {
    private int id;
    private String titulo;
    private String autor;
    private String tipo;
    private String estado;

    public Documento(int id, String titulo, String autor, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Documento(String titulo, String autor, String tipo, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return titulo + " - " + autor + " [" + tipo + "] - " + estado;
    }
}