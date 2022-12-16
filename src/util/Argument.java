/**
 * @file src/util/Argument.java
 * @brief Classe Argument qui represente un argument
 * @author Youcef MEDILEH
 * @version 1.0
 */

package util;

public class Argument implements Comparable {
    private String nom;

    public Argument(String s) {
        this.nom = s;
    }

    public String getNom() {
        return nom;
    }

    /**
     * @brief Compare deux arguments
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Argument arg = (Argument) o;
        return this.nom.compareTo(arg.getNom());
    }
}
