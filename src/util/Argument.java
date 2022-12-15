package util;

public class Argument implements Comparable {
    private String nom;

    public Argument(String s) {
        this.nom = s;
    }

    public String getNom() {
        return nom;
    }


    @Override
    public int compareTo(Object o) {
        Argument arg = (Argument) o;
        return this.nom.compareTo(arg.getNom());
    }
}
