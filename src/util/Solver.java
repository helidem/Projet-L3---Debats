package util;

import java.util.ArrayList;
import java.util.Collections;

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
        // verifier si la solution est vide
        if (solution.isEmpty())
            return true;
        else {
            // verifier si les arguments dans la solution ne sont pas en conflit (en contradiction entre eux)
            for (int i = 0; i < solution.size(); i++) {
                for (int j = i + 1; j < solution.size(); j++) {
                    if (graphe.AcontreditB(solution.get(i), solution.get(j)) || graphe.AcontreditB(solution.get(j), solution.get(i)))
                        return false;
                }
            }
            // recherche d'element du graphe qui n'est pas dans la solution et qui contredit un element de la solution
            ArrayList<Argument> arguments = graphe.getArguments();
            for (int i = 0; i < arguments.size(); i++) { // pour chaque argument du graphe
                if (!estDansSolution(arguments.get(i), solution)) { // si l'argument n'est pas dans la solution
                    for (int j = 0; j < solution.size(); j++) { // alors pour chaque argument de la solution
                        if (graphe.AcontreditB(arguments.get(i), solution.get(j))) { // si l'argument du graphe contredit un argument de la solution
                            // alors on cherche un element de la solution qui contredit cet argument (l'argument du graphe)
                            boolean trouve = false;
                            for (int k = 0; k < solution.size(); k++) {
                                if (graphe.AcontreditB(solution.get(k), arguments.get(i))) {
                                    // si on trouve un element de la solution qui contredit l'argument du graphe alors l'argument est admissible
                                    trouve = true;
                                    break;
                                }
                            }
                            // si on a pas trouvé d'element de la solution qui contredit l'argument du graphe alors l'argument n'est pas admissible
                            if (!trouve) {
                                return false;
                            }
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
     *
     * @param graphe le graphe
     * @return la liste des ensemble de solutions admissibles
     */
    public static ArrayList<ArrayList<Argument>> calculSolutionsAdmissibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<>();
        ArrayList<ArrayList<Argument>> possibilites;
        possibilites = calculToutesCombinaisonsPossibles(graphe);
        for (ArrayList<Argument> solution : possibilites) {
            calculSolutionsAdmissiblesRecursif(graphe, solution, solutions);
        }
        return solutions;
    }

    private static void calculSolutionsAdmissiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        // les ensemble de solutions admissibles doivent êtres unique, {A, C} et {C, A} sont la même solution
        // pour vérifier cela on trie les arguments de la solution et on vérifie si la solution existe déjà
        Collections.sort(solution);
        if (!solutions.contains(solution)) {
            if (verifierSolutionAdmissible(graphe, solution)) {
                solutions.add(new ArrayList<Argument>(solution));
                ArrayList<Argument> arguments = graphe.getArguments();
                for (Argument argument : arguments) {
                    if (!estDansSolution(argument, solution)) {
                        solution.add(argument);
                        calculSolutionsAdmissiblesRecursif(graphe, solution, solutions);
                        retirerArgument(argument, solution);
                    }
                }
            }
        }
    }

    public static ArrayList<ArrayList<Argument>> calculToutesCombinaisonsPossibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<ArrayList<Argument>>();
        ArrayList<Argument> solution = new ArrayList<Argument>();
        calculToutesCombinaisonsPossiblesRecursif(graphe, solution, solutions);
        System.out.println("Nombre de solutions : " + solutions.size());
        return solutions;
    }

    private static void calculToutesCombinaisonsPossiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        // les ensemble de solutions admissibles doivent êtres unique, {A, C} et {C, A} sont la même solution
        // pour vérifier cela on trie les arguments de la solution et on vérifie si la solution existe déjà
        Collections.sort(solution);
        if (!solutions.contains(solution)) {
            solutions.add(new ArrayList<Argument>(solution));
            ArrayList<Argument> arguments = graphe.getArguments();
            for (Argument argument : arguments) {
                if (!estDansSolution(argument, solution)) {
                    solution.add(argument);
                    calculToutesCombinaisonsPossiblesRecursif(graphe, solution, solutions);
                    retirerArgument(argument, solution);
                }
            }
        }
    }

}
