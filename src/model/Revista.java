package model;

public class Revista extends Documento {
    public Revista(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "revista");
    }
}