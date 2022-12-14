package util;

import java.util.ArrayList;

public class Solver {


    /**
     * Verifie si un argument est dans la solution
     *
     * @param arg      l'argument à vérifier
     * @param solution la solution
     * @return true si l'argument est dans la solution, false sinon
     */
    public static boolean estDansSolution(Argument arg, ArrayList<Argument> solution) {
        int nbElements = solution.size();
        for (int i = 0; i < nbElements; i++) {
            if (solution.get(i).equals(arg))
                return true;
        }
        return false;
    }


    /*
    pseudo code
    if not verifierSolutionAdmissible(Graphe graphe, solution) then
        return false
    else
        for each ensemble E' inclut dans graphe tel que solution est inclut dans E' do
            if E' est une solution admissible then
                return false
            end if
        end for
    end if
    return true
     */
    public static boolean verifierSolutionPreferee(Graphe graphe, ArrayList<Argument> solution) {
        int nbElements = solution.size();
        if (!verifierSolutionAdmissible(graphe, solution))
            return false;
        else {
            for (int i = 0; i < nbElements; i++) {
                // si l'ensemble E' est inclut dans la solution alors c'est pas une solution preferee
                if (solution.get(i).getEnsemble().containsAll(solution))
                    return false;
                ArrayList<Argument> solutionTemp = new ArrayList<Argument>();
                solutionTemp.add(solution.get(i));
                if (verifierSolutionAdmissible(graphe, solutionTemp))
                    return false;
            }
        }
        return true;
    }


    /**
     * Verifie si une solution est admissible
     *
     * @param graphe   le graphe
     * @param solution la solution
     * @return true si la solution est admissible, false sinon
     */
    public static boolean verifierSolutionAdmissible(Graphe graphe, ArrayList<Argument> solution) {
        int nbElement = solution.size();
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
     * @param solution la solution
     */
    public static void afficherSolution(ArrayList<Argument> solution) {
        int nbElement = solution.size();
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
     * @param arg      l'argument à retirer
     * @param solution la solution
     */
    public static void retirerArgument(Argument arg, ArrayList<Argument> solution) {
        for (Argument argument : solution) {
            if (argument.getNom().equals(arg.getNom())) {
                solution.remove(argument);
                break;
            }
        }
    }

    /**
     * Calcule toutes les solutions admissibles possibles dans le graphe
     * @param graphe le graphe
     * @return la liste des ensemble de solutions admissibles
     */
    public static ArrayList<ArrayList<Argument>> calculSolutionsAdmissibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<ArrayList<Argument>>();
        ArrayList<Argument> solution = new ArrayList<Argument>();
        calculSolutionsAdmissiblesRecursif(graphe, solution, solutions);
        return solutions;
    }

    private static void calculSolutionsAdmissiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        if (verifierSolutionAdmissible(graphe, solution)) {
            solutions.add(solution);
        }
        for (Argument arg : graphe.getArguments()) {
            if (!estDansSolution(arg, solution)) {
                ArrayList<Argument> solutionTemp = new ArrayList<Argument>(solution);
                solutionTemp.add(arg);
                calculSolutionsAdmissiblesRecursif(graphe, solutionTemp, solutions);
            }
        }
    }

}
