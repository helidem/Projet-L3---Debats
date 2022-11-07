package appli;

import util.Argument;
import util.Graphe;

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

        // tableau des arguments
        Argument[] solution = new Argument[arguments.size()];
        int nbElement = 0;
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
                        if(estDansSolution(arg, solution, nbElement)) {
                            System.out.println("L'argument est deja dans la solution");
                        } else {
                            solution[nbElement] = arg;
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
                        if(estDansSolution(arg, solution, nbElement)) {
                            retirerArgument(arg, solution, nbElement);
                            nbElement--;
                        } else {
                            System.out.println("L'argument n'est pas dans la solution");
                        }

                    } else {
                        System.out.println("Argument inexistant");
                    }
                    break;
                case 3:
                    if (verifierSolution(graphe, solution, nbElement))
                        System.out.println("Solution admissible");
                    else
                        System.out.println("Solution non admissible");
                case 4:
                    afficherSolution(solution, nbElement);
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }


        } while (choix != 4);

    }

    private static void retirerArgument(Argument arg, Argument[] solution, int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            if (solution[i].equals(arg)) {
                // solution[i] = solution[nbElement - 1];
                solution[i] = null;
                nbElement--;
                break;
            }
        }
    }

    private static boolean estDansSolution(Argument arg, Argument[] solution, int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            if (solution[i].equals(arg))
                return true;
        }
        return false;
    }

    private static boolean verifierSolution(Graphe graphe, Argument[] solution, int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            for (int j = i + 1; j < nbElement; j++) {
                if (graphe.estContradiction(solution[i], solution[j])) {
                    return false;
                }
            }
        }
        // recherche d'element du graphe qui n'est pas dans la solution et qui contredit un element de la solution
        for (Argument arg : graphe.getArguments()) {
            boolean trouve = false;
            for (int i = 0; i < nbElement; i++) {
                if (solution[i].equals(arg)) {
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                for (int i = 0; i < nbElement; i++) {
                    if (graphe.estContradiction(arg, solution[i])) {
                        // si cet element contredit un element de la solution, on cherche un autre element de la solution qui contredit cet element
                        boolean trouve2 = false;
                        for (int j = 0; j < nbElement; j++) {
                            if (graphe.estContradiction(solution[j], arg)) {
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

    private static void afficherSolution(Argument[] solution, int nbElement) {
        System.out.print("Solution : {");
        for (int i = 0; i < nbElement; i++) {
            System.out.print(solution[i].getNom());
            if (i != nbElement - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}\n");
    }

}
