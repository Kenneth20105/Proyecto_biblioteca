package model;

public class CD extends Documento {
    public CD(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "cd");
    }

    public CD(String titulo, String autor, String estado) {
        super(titulo, autor, "cd", estado);
    }
}