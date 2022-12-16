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
        do {
            // ajout des arguments dans l'ensemble des solutions
            System.out.println("1. Ajouter un argument");
            // retirer un argument de l'ensemble des solutions
            System.out.println("2. Retirer un argument");
            // verifier si l'ensemble des solutions est une solution admissible
            System.out.println("3. Verifier la solution");
            System.out.println("4. Fin");
            System.out.println("5. Verifier si la solution est preferee");
            System.out.println("6. Afficher les solutions admissibles");
            System.out.println("7. Afficher les solutions preferees");
            choix = verifEntreeInt(sc);
            Solver solver = new Solver(graphe);

            switch (choix) {
                case 1:
                    System.out.print("Entre le nom de l'argument : ");
                    String nom = sc.next();
                    Argument arg = graphe.getArgument(nom);
                    if (arg != null) {
                        if (solution.contains(arg)) {
                            System.out.println("L'argument est deja dans la solution");
                        } else {
                            solution.add(arg);
                        }
                    } else {
                        System.out.println("Argument inexistant");
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
                            System.out.println("L'argument n'est pas dans la solution");
                        }

                    } else {
                        System.out.println("Argument inexistant");
                    }
                    break;
                case 3:
                    if (solver.estSolutionAdmissible(graphe, solution))
                        System.out.println("Solution admissible");
                    else
                        System.out.println("Solution non admissible");
                case 4:
                    solver.afficherSolution(solution);
                    break;
                case 5:
                    if (solver.estSolutionPreferee(graphe, solution))
                        System.out.println("Solution preferee");
                    else
                        System.out.println("Solution non preferee");
                    break;
                case 6:

                    for (ArrayList<Argument> sol : solver.getLaListeDeToutesLesSolutionsAdmissibles()) {
                        solver.afficherSolution(sol);
                    }
                    break;
                case 7:
                    System.out.println("Toutes les solutions preferees");
                    ArrayList<ArrayList<Argument>> solutionsPreferees = solver.calculToutesLesSolutionsPreferees(graphe);
                    for (ArrayList<Argument> sol : solutionsPreferees) {
                        solver.afficherSolution(sol);
                    }
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }


        } while (choix != 4);
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
