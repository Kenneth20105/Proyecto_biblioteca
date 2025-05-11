package model;

public class Libro extends Documento {
    public Libro(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "libro");
    }

    public Libro(String titulo, String autor, String estado) {
        super(titulo, autor, "libro", estado);
    }
}