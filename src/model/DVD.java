package model;

public class DVD extends Documento {
    public DVD(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "dvd");
    }

    public DVD(String titulo, String autor, String estado) {
        super(titulo, autor, "dvd", estado);
    }
}