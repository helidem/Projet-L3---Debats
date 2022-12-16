/**
 * @file src\appli\Appli.java
 * @brief Classe principale de l'application
 * @author Youcef MEDILEH
 * @version 1.0
 */

package appli;

import util.Argument;
import util.Graphe;
import util.Solver;

import java.util.ArrayList;
import java.util.Scanner;


public class Appli {
    public static void main(String[] args) {
        System.out.println("====== Debats ======");
        System.out.print("Entrez le nom du fichier contenant le graphe :");
        Scanner sc = new Scanner(System.in);

        // au lieu de creer les arguments, on les récupère depuis le fichier
        Graphe graphe = new Graphe(sc);

        ArrayList<Argument> solution = new ArrayList<>();
        menuSolution(sc, graphe, solution);
    }

    /**
     * @param sc       le scanner
     * @param graphe   le graphe
     * @param solution la solution
     * @brief Menu pour ajouter des contradictions, autrement dit, ajouter des arcs
     */
    private static void menuSolution(Scanner sc, Graphe graphe, ArrayList<Argument> solution) {
        int choix;
        Solver solver = new Solver(graphe);
        do {
            System.out.println("========== MENU SOLUTIONS ==========");
            System.out.print("Actuellement, la solution est : ");
            solver.afficherSolution(solution);
            // ajout des arguments dans l'ensemble des solutions
            System.out.println("1. Ajouter un argument");
            // retirer un argument de l'ensemble des solutions
            System.out.println("2. Retirer un argument");
            // verifier si l'ensemble des solutions est une solution admissible
            System.out.println("3. Verifier la solution est admissible");
            System.out.println("4. Verifier si la solution est preferee");
            System.out.println("5. Afficher les solutions admissibles");
            System.out.println("6. Afficher les solutions preferees");
            System.out.println("7. Fin (affiche la solution actuelle)");
            choix = verifEntreeInt(sc);


            switch (choix) {
                case 1:
                    System.out.print("Entre le nom de l'argument : ");
                    String nom = sc.next();
                    Argument arg = graphe.getArgument(nom);
                    if (arg != null) {
                        if (solution.contains(arg)) {
                            System.out.println("[ERREUR] L'argument est deja dans la solution");
                        } else {
                            solution.add(arg);
                        }
                    } else {
                        System.out.println("[ERREUR] Argument inexistant");
                    }
                    break;
                case 2:
                    System.out.print("Entre le nom de l'argument : ");
                    nom = sc.next();
                    arg = graphe.getArgument(nom);
                    if (arg != null) {
                        if (solution.contains(arg)) {
                            solution.remove(arg);
                        } else {
                            System.out.println("[ERREUR] L'argument n'est pas dans la solution");
                        }

                    } else {
                        System.out.println("[ERREUR] Argument inexistant");
                    }
                    break;
                case 3:
                    if (solver.estSolutionAdmissible(graphe, solution))
                        System.out.println("La solution est admissible");
                    else System.out.println("La solution n'est pas admissible");

                case 4:
                    if (solver.estSolutionPreferee(graphe, solution)) {
                        System.out.println("La solution proposée est preferee");
                        solver.afficherSolution(solution);
                    } else {
                        System.out.println("La solution proposée n'est pas preferee");
                        solver.afficherSolution(solution);
                    }
                    break;
                case 5:
                    System.out.println("====== Voici la liste de toutes les solutions admissibles : ======");
                    for (ArrayList<Argument> sol : solver.getLaListeDeToutesLesSolutionsAdmissibles()) {
                        solver.afficherSolution(sol);
                    }
                    break;
                case 6:
                    System.out.println("====== Voici la liste de toutes les solutions preferees : ======");
                    ArrayList<ArrayList<Argument>> solutionsPreferees = solver.calculToutesLesSolutionsPreferees(graphe);
                    for (ArrayList<Argument> sol : solutionsPreferees) {
                        solver.afficherSolution(sol);
                    }
                    break;
                case 7:
                    System.out.println("Fin");
                    System.out.println("====== Voici la solution finale : ======");
                    solver.afficherSolution(solution);
                    break;
                default:
                    System.out.println("[ERREUR] Choix invalide");
                    break;
            }
        } while (choix != 7);
    }

    /**
     * @param sc le scanner
     * @return l'entree
     * @brief Verifie si l'entree est un entier
     */
    private static int verifEntreeInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Erreur : entrez un nombre entier");
            sc.next();
        }
        return sc.nextInt();
    }
}
