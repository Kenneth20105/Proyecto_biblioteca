package model;

public class DVD extends Documento {
    public DVD(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "dvd");
    }
}