package model;

public class Revista extends Documento {
    public Revista(int id, String titulo, String autor, String estado) {
        super(id, titulo, autor, "revista");
    }

   // public Revista(String titulo, String autor, String estado) {
        //super(titulo, autor, "revista", estado);
    //}
}