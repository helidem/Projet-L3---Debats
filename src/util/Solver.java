/**
 * @file src/util/Solver.java
 * @brief Classe Solver qui permet de resoudre le probleme
 * @author Youcef MEDILEH
 */

package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
     * @param graphe   le graphe
     * @param solution la solution
     * @return true si la solution est preferee, false sinon
     * @brief Verifie si la solution est preferee
     */
    public boolean estSolutionPreferee(Graphe graphe, ArrayList<Argument> solution) {
        int nbElements = solution.size();
        if (nbElements == 0) return false;
        if (!estSolutionAdmissible(graphe, solution)) return false;
        else {
            for (ArrayList<Argument> uneSolutionAdmissible : laListeDeToutesLesSolutionsAdmissibles) { // pour chaque solution admissible
                // on regarde si c'est pas le meme ensemble
                if (!uneSolutionAdmissible.equals(solution)) {
                    if (uneSolutionAdmissible.containsAll(solution)) return false;
                }
            }
        }
        return true;
    }

    /**
     * @param graphe   le graphe
     * @param solution la solution
     * @return true si la solution est admissible, false sinon
     * @brief Verifie si une solution est admissible
     */
    public boolean estSolutionAdmissible(Graphe graphe, ArrayList<Argument> solution) {
        // verifier si la solution est vide
        if (solution.isEmpty()) return true;
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
     * @param solution la solution
     * @brief Affiche la solution
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
        System.out.println("}");
    }

    /**
     * @param graphe le graphe
     * @return la liste des ensemble de solutions admissibles
     * @brief Calcule toutes les solutions admissibles possibles dans le graphe
     */
    public ArrayList<ArrayList<Argument>> calculToutesLesSolutionsAdmissibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> solutions = new ArrayList<>(); // la liste des solutions admissibles
        ArrayList<ArrayList<Argument>> possibilites; // la liste des possibilites
        possibilites = calculToutesCombinaisonsPossibles(graphe); // on calcule toutes les combinaisons possibles
        for (ArrayList<Argument> solution : possibilites) { // pour chaque solution
            calculToutesLesSolutionsAdmissiblesRecursif(graphe, solution, solutions); // on calcule toutes les solutions admissibles
        }
        return solutions;
    }

    /**
     * @param graphe    le graphe
     * @param solution  la solution
     * @param solutions la liste des solutions
     * @brief Calcule toutes les solutions admissibles possibles dans le graphe (recursif)
     */
    private void calculToutesLesSolutionsAdmissiblesRecursif(Graphe graphe, ArrayList<Argument> solution, ArrayList<ArrayList<Argument>> solutions) {
        Collections.sort(solution); // trier la solution pour eviter les doublons
        if (!solutions.contains(solution)) { // si la solution n'est pas deja dans la liste des solutions
            if (estSolutionAdmissible(graphe, solution)) { // si la solution est admissible
                solutions.add(new ArrayList<Argument>(solution)); // on ajoute la solution a la liste des solutions
                ArrayList<Argument> arguments = graphe.getArguments(); // on recupere la liste des arguments du graphe
                for (Argument argument : arguments) { // pour chaque argument du graphe
                    if (!solution.contains(argument)) { // si l'argument n'est pas dans la solution
                        solution.add(argument); // on ajoute l'argument a la solution
                        calculToutesLesSolutionsAdmissiblesRecursif(graphe, solution, solutions); // on appelle la fonction recursivement
                        solution.remove(argument); // on retire l'argument de la solution
                    }
                }
            }
        }
    }

    /**
     * @param graphe le graphe
     * @return la liste des ensemble de combinations possibles
     * @brief Calcule toutes les combinaisons possibles dans le graphe
     */
    public ArrayList<ArrayList<Argument>> calculToutesCombinaisonsPossibles(Graphe graphe) {
        ArrayList<ArrayList<Argument>> combinaisons = new ArrayList<ArrayList<Argument>>();
        ArrayList<Argument> combinaison = new ArrayList<Argument>();
        calculToutesCombinaisonsPossiblesRecursif(graphe, combinaison, combinaisons);
        return combinaisons;
    }

    /**
     * @param graphe       le graphe
     * @param combinaison  la combinaison
     * @param combinaisons la liste des combinaisons
     * @brief Calcule toutes les combinaisons possibles dans le graphe (fonction recursive)
     */
    private void calculToutesCombinaisonsPossiblesRecursif(Graphe graphe, ArrayList<Argument> combinaison, ArrayList<ArrayList<Argument>> combinaisons) {
        Collections.sort(combinaison);
        if (!combinaisons.contains(combinaison)) {
            combinaisons.add(new ArrayList<Argument>(combinaison));
            ArrayList<Argument> arguments = graphe.getArguments();
            for (Argument argument : arguments) {
                if (!combinaison.contains(argument)) {
                    combinaison.add(argument);
                    calculToutesCombinaisonsPossiblesRecursif(graphe, combinaison, combinaisons);
                    combinaison.remove(argument);
                }
            }
        }
    }


    /**
     * @param graphe le graphe
     * @return la liste des ensemble de solutions preferees
     * @brief Calcule toutes les solutions preferees possibles dans le graphe
     */
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

    public void sauvegarderSolution(ArrayList<Argument> solution, String nomFichier) {
        try {
            FileWriter fw = new FileWriter(nomFichier);
            BufferedWriter bw = new BufferedWriter(fw);
            int nbElement = solution.size();
            bw.write("Solution : {");
            for (int i = 0; i < nbElement; i++) {
                bw.write(solution.get(i).getNom());
                if (i != nbElement - 1) {
                    bw.write(", ");
                }
            }
            bw.write("}");
            bw.close();
        } catch (IOException e) {
            System.out.println("[ERREUR] Impossible de sauvegarder la solution");
        }
    }

    public void sauvegarderSolutionsAdmissibles(String nomFichier) {
        try {
            FileWriter fw = new FileWriter(nomFichier);
            BufferedWriter bw = new BufferedWriter(fw);
            int nbElement = laListeDeToutesLesSolutionsAdmissibles.size();
            bw.write("Solutions admissibles : {");
            for (int i = 0; i < nbElement; i++) {
                bw.write("{");
                ArrayList<Argument> solution = laListeDeToutesLesSolutionsAdmissibles.get(i);
                int nbElementSolution = solution.size();
                for (int j = 0; j < nbElementSolution; j++) {
                    bw.write(solution.get(j).getNom());
                    if (j != nbElementSolution - 1) {
                        bw.write(", ");
                    }
                }
                bw.write("}");
                if (i != nbElement - 1) {
                    bw.write(", ");
                }
            }
            bw.write("}");
            bw.close();
        } catch (IOException e) {
            System.out.println("[ERREUR] Impossible de sauvegarder les solutions admissibles");
        }
    }

    public void sauvegarderSolutionsPreferees(String nomFichier) {
        try {
            FileWriter fw = new FileWriter(nomFichier);
            BufferedWriter bw = new BufferedWriter(fw);
            ArrayList<ArrayList<Argument>> laListeDeToutesLesSolutionsPreferees = calculToutesLesSolutionsPreferees(graphe);
            int nbElement = laListeDeToutesLesSolutionsPreferees.size();
            bw.write("Solutions preferees : {");
            for (int i = 0; i < nbElement; i++) {
                bw.write("{");
                ArrayList<Argument> solution = laListeDeToutesLesSolutionsPreferees.get(i);
                int nbElementSolution = solution.size();
                for (int j = 0; j < nbElementSolution; j++) {
                    bw.write(solution.get(j).getNom());
                    if (j != nbElementSolution - 1) {
                        bw.write(", ");
                    }
                }
                bw.write("}");
                if (i != nbElement - 1) {
                    bw.write(", ");
                }
            }
            bw.write("}");
            bw.close();
        } catch (IOException e) {
            System.out.println("[ERREUR] Impossible de sauvegarder les solutions preferees");
        }
    }
}
