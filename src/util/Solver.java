package util;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private Graphe graphe;
    private ArrayList<ArrayList<Argument>> laListeDeToutesLesSolutionsAdmissibles;

    public Solver(Graphe graphe) {
        this.graphe = graphe;
        this.laListeDeToutesLesSolutionsAdmissibles = calculToutesLesSolutionsAdmissibles(this.graphe);
    }

    /**
     * Verifie si la solution est preferee
     *
     * @param graphe   le graphe
     * @param solution la solution
     * @return true si la solution est preferee, false sinon
     */
    public boolean estSolutionPreferee(Graphe graphe, ArrayList<Argument> solution) {
        int nbElements = solution.size();
        if (nbElements == 0)
            return false;
        if (!estSolutionAdmissible(graphe, solution))
            return false;
        else {
            for (ArrayList<Argument> uneSolutionAdmissible : laListeDeToutesLesSolutionsAdmissibles) {
                // on regarde si c'est pas le meme ensemble
                if (!uneSolutionAdmissible.equals(solution)) {
                    if (uneSolutionAdmissible.containsAll(solution))
                        return false;
                }
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
    public boolean estSolutionAdmissible(Graphe graphe, ArrayList<Argument> solution) {
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
                if (!solution.contains(arguments.get(i))) { // si l'argument n'est pas dans la solution
                    for (int j = 0; j < solution.size(); j++) { // alors pour chaque argument de la solution
                        if (graphe.AcontreditB(arguments.get(i), solution.get(j))) { // si l'argument du graphe contredit un argument de la solution
                            // alors on cherche un element de la solution qui contredit cet argument (l'argument du graphe)
                            boolean trouve = false; // pour savoir si on a trouve un element de la solution qui contredit l'argument du graphe
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
    public void afficherSolution(ArrayList<Argument> solution) {
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
    public void retirerArgument(Argument arg, ArrayList<Argument> solution) {
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
    public ArrayList<ArrayList<Argument>> calculToutesLesSolutionsAdmissibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<>();
        ArrayList<ArrayList<Argument>> possibilites;
        possibilites = calculToutesCombinaisonsPossibles(graphe);
        for (ArrayList<Argument> solution : possibilites) {
            calculToutesLesSolutionsAdmissiblesRecursif(graphe, solution, solutions);
        }
        return solutions;
    }

    private void calculToutesLesSolutionsAdmissiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        // les ensemble de solutions admissibles doivent êtres unique, {A, C} et {C, A} sont la même solution
        // pour vérifier cela on trie les arguments de la solution et on vérifie si la solution existe déjà
        Collections.sort(solution);
        if (!solutions.contains(solution)) {
            if (estSolutionAdmissible(graphe, solution)) {
                solutions.add(new ArrayList<Argument>(solution));
                ArrayList<Argument> arguments = graphe.getArguments();
                for (Argument argument : arguments) {
                    if (!solution.contains(argument)) {
                        solution.add(argument);
                        calculToutesLesSolutionsAdmissiblesRecursif(graphe, solution, solutions);
                        retirerArgument(argument, solution);
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Argument>> calculToutesCombinaisonsPossibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<ArrayList<Argument>>();
        ArrayList<Argument> solution = new ArrayList<Argument>();
        calculToutesCombinaisonsPossiblesRecursif(graphe, solution, solutions);
        return solutions;
    }

    private void calculToutesCombinaisonsPossiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        // les ensemble de solutions admissibles doivent êtres unique, {A, C} et {C, A} sont la même solution
        // pour vérifier cela on trie les arguments de la solution et on vérifie si la solution existe déjà
        Collections.sort(solution);
        if (!solutions.contains(solution)) {
            solutions.add(new ArrayList<Argument>(solution));
            ArrayList<Argument> arguments = graphe.getArguments();
            for (Argument argument : arguments) {
                if (!solution.contains(argument)) {
                    solution.add(argument);
                    calculToutesCombinaisonsPossiblesRecursif(graphe, solution, solutions);
                    retirerArgument(argument, solution);
                }
            }
        }
    }

    public ArrayList<ArrayList<Argument>> calculToutesLesSolutionsPreferees(Graphe graphe) {
        // pour chaque solution admissible on vérifie si elle est préférée
        ArrayList<ArrayList<Argument>> toutesLesSolutionsAdmissibles = calculToutesLesSolutionsAdmissibles(graphe);
        ArrayList<ArrayList<Argument>> toutesLesSolutionsPreferees = new ArrayList<>();
        for (ArrayList<Argument> solutionAdmissible : toutesLesSolutionsAdmissibles) {
            if (estSolutionPreferee(graphe, solutionAdmissible)) {
                toutesLesSolutionsPreferees.add(solutionAdmissible);
            }
        }
        return toutesLesSolutionsPreferees;
    }

    public ArrayList<ArrayList<Argument>> getLaListeDeToutesLesSolutionsAdmissibles() {
        return laListeDeToutesLesSolutionsAdmissibles;
    }
}
