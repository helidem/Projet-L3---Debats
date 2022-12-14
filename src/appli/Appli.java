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



        // au lieu de creer les arguments, on les recupere depuis le fichier
        Graphe graphe = new Graphe(sc);


        // creation du graphe
        //Graphe graphe = new Graphe(arguments);
        //menuAjoutContradiction(sc, graphe);

        // tableau des arguments
        ArrayList<Argument> solution = new ArrayList<>();
        menuSolution(sc, graphe, solution);

    }

    /**
     * Menu pour ajouter des contradictions, autrement dit, ajouter des arcs
     *
     * @param sc        le scanner
     * @param graphe    le graphe
     * @param solution  la solution
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
            choix = verifEntreeInt(sc);

            switch (choix) {
                case 1:
                    System.out.print("Entre le nom de l'argument : ");
                    String nom = sc.next();
                    Argument arg = graphe.getArgument(nom);
                    if (arg != null) {
                        if (Solver.estDansSolution(arg, solution)) {
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
                        if (Solver.estDansSolution(arg, solution)) {
                            Solver.retirerArgument(arg, solution);

                        } else {
                            System.out.println("L'argument n'est pas dans la solution");
                        }

                    } else {
                        System.out.println("Argument inexistant");
                    }
                    break;
                case 3:
                    if (Solver.verifierSolutionAdmissible(graphe, solution))
                        System.out.println("Solution admissible");
                    else
                        System.out.println("Solution non admissible");
                case 4:
                    Solver.afficherSolution(solution);
                    break;
                case 5:
                    if (Solver.verifierSolutionPreferee(graphe, solution))
                        System.out.println("Solution preferee");
                    else
                        System.out.println("Solution non preferee");
                    break;
                case 6:
                    ArrayList<ArrayList<Argument>> solutions =   Solver.calculSolutionsAdmissibles(graphe);
                    for (ArrayList<Argument> sol : solutions) {
                        Solver.afficherSolution(sol);
                    }

                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }


        } while (choix != 4);
    }

    /**
     * Menu pour ajouter des contradictions
     *
     * @param sc     le scanner
     * @param graphe le graphe
     */
    private static void menuAjoutContradiction(Scanner sc, Graphe graphe) {
        int choix;
        do {
            // ajout des contradictions
            // une contradiction est représentée par une arete entre deux arguments (l'ensemble des contradictions et argumentsest un graphe)
            System.out.println("1. Ajouter une contradiction");
            System.out.println("2. Fin"); // on passe au menu suivant
            choix = verifEntreeInt(sc);
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

    private static int verifEntreeInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Erreur : entrez un nombre entier");
            sc.next();
        }
        return sc.nextInt();
    }
}
