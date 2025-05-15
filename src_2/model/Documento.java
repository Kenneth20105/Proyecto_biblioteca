
package model;

public class Documento {
    protected int id;
    protected String titulo;
    protected String autor;
    protected int anioPublicacion;
    protected String tipo;

    public Documento(int id, String titulo, String autor, int anioPublicacion, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.tipo = tipo;
    }

    public Documento(String titulo, String autor, int anioPublicacion, String tipo) {
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.tipo = tipo;
    }

    public Documento() {}

    // Getters y setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public String getTipo() { return tipo; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
