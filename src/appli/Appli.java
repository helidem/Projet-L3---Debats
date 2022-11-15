package appli;

import util.Argument;
import util.Graphe;
import util.Solver;

import java.util.ArrayList;
import java.util.Scanner;

public class Appli {
    public static void main(String[] args) {
        System.out.println("====== Debats ======");
        System.out.print("Entre le nombre d'arguments' : ");
        Scanner sc = new Scanner(System.in);
        int nbArg = sc.nextInt();

        // creation des arguments
        ArrayList<Argument> arguments = new ArrayList<>();
        for (int i = 1; i <= nbArg; i++) {
            arguments.add(new Argument("A" + i));
        }

        // creation du graphe
        Graphe graphe = new Graphe(arguments);
        menuAjoutContradiction(sc, graphe);
        int choix;

        // tableau des arguments
        ArrayList<Argument> solution = new ArrayList<>();
        int nbElement = 0;
        menuSolution(sc, graphe, solution, nbElement);

    }

    /**
     * Menu pour ajouter des contradictions
     * @param sc le scanner
     * @param graphe le graphe
     * @param solution la solution
     * @param nbElement le nombre d'éléments dans la solution
     */
    private static void menuSolution(Scanner sc, Graphe graphe, ArrayList<Argument> solution, int nbElement) {
        int choix;
        do {
            // ajout des arguments dans l'ensemble des solutions
            System.out.println("1. Ajouter un argument");
            // retirer un argument de l'ensemble des solutions
            System.out.println("2. Retirer un argument");
            // verifier si l'ensemble des solutions est une solution admissible
            System.out.println("3. Verifier la solution");
            System.out.println("4. Fin");
            choix = sc.nextInt();

            switch (choix) {
                case 1:
                    System.out.print("Entre le nom de l'argument : ");
                    String nom = sc.next();
                    Argument arg = graphe.getArgument(nom);
                    if (arg != null) {
                        if (Solver.estDansSolution(arg, solution, nbElement)) {
                            System.out.println("L'argument est deja dans la solution");
                        } else {
                            solution.add(nbElement, arg);
                            nbElement++;
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
                        if (Solver.estDansSolution(arg, solution, nbElement)) {
                            Solver.retirerArgument(arg, solution, nbElement);
                            nbElement--;
                        } else {
                            System.out.println("L'argument n'est pas dans la solution");
                        }

                    } else {
                        System.out.println("Argument inexistant");
                    }
                    break;
                case 3:
                    if (Solver.verifierSolution(graphe, solution, nbElement))
                        System.out.println("Solution admissible");
                    else
                        System.out.println("Solution non admissible");
                case 4:
                    Solver.afficherSolution(solution, nbElement);
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }


        } while (choix != 4);
    }

    /**
     * Menu pour ajouter des contradictions
     * @param sc le scanner
     * @param graphe le graphe
     */
    private static void menuAjoutContradiction(Scanner sc, Graphe graphe) {
        int choix;
        do {
            // ajout des contradictions
            // une contradiction est représentée par une arete entre deux arguments (l'ensemble des contradictions et argumentsest un graphe)
            System.out.println("1. Ajouter une contradiction");
            System.out.println("2. Fin"); // on passe au menu suivant
            choix = sc.nextInt();
            switch (choix) {
                case 1:
                    System.out.print("Entre le premier argument : ");
                    String arg1 = sc.next();
                    System.out.print("Entre le deuxieme argument : ");
                    String arg2 = sc.next();
                    graphe.ajouterContradiction(arg1, arg2);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        } while (choix != 2);
    }
}
