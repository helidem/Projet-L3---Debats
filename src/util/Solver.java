package util;

import java.util.ArrayList;

public class Solver {




    /**
     * Verifie si un argument est dans la solution
     *
     * @param arg       l'argument à vérifier
     * @param solution  la solution
     * @param nbElement le nombre d'éléments dans la solution
     * @return true si l'argument est dans la solution, false sinon
     */
    public static boolean estDansSolution(Argument arg, ArrayList<Argument> solution, int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            if (solution.get(i).equals(arg))
                return true;
        }
        return false;
    }

    /**
     * Verifie si une solution est admissible
     *
     * @param graphe    le graphe
     * @param solution  la solution
     * @param nbElement le nombre d'éléments dans la solution
     * @return true si la solution est admissible, false sinon
     */
    public static boolean verifierSolution(Graphe graphe, ArrayList<Argument> solution, int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            for (int j = i + 1; j < nbElement; j++) {
                if (graphe.estContradiction(solution.get(i), solution.get(j))) {
                    return false;
                }
            }
        }
        // recherche d'element du graphe qui n'est pas dans la solution et qui contredit un element de la solution
        for (Argument arg : graphe.getArguments()) {
            boolean trouve = false;
            for (int i = 0; i < nbElement; i++) {
                if (solution.get(i).equals(arg)) {
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                for (int i = 0; i < nbElement; i++) {
                    if (graphe.estContradiction(arg, solution.get(i))) {
                        // si cet element contredit un element de la solution, on cherche un autre element dans la solution qui contredit cet element
                        boolean trouve2 = false;
                        for (int j = 0; j < nbElement; j++) {
                            if (graphe.estContradiction(solution.get(j), arg)) {
                                trouve2 = true;
                                break;
                            }
                        }
                        if (!trouve2) { // il n'a pas trouve d'autre element de la solution qui contredit cet element
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Affiche la solution
     *
     * @param solution  la solution
     * @param nbElement le nombre d'éléments dans la solution
     */
    public static void afficherSolution(ArrayList<Argument> solution, int nbElement) {
        System.out.print("Solution : {");
        for (int i = 0; i < nbElement; i++) {
            System.out.print(solution.get(i).getNom());
            if (i != nbElement - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}\n");
    }

    /**
     * Retire un argument de la solution
     *
     * @param arg       l'argument à retirer
     * @param solution  la solution
     * @param nbElement le nombre d'éléments dans la solution
     */
    public static void retirerArgument(Argument arg, ArrayList<Argument> solution, int nbElement) {
        for (Argument argument : solution) {
            if (argument.getNom().equals(arg.getNom())) {
                solution.remove(argument);
                break;
            }
        }
    }


}
