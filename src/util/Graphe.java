package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graphe {
    private boolean[][] graphe;
    private ArrayList<Argument> arguments;

    public Graphe(ArrayList<Argument> arguments) {
        graphe = new boolean[arguments.size()][arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            for (int j = 0; j < arguments.size(); j++) {
                graphe[i][j] = false;
            }
        }
        this.arguments = arguments;
    }

    public Graphe(Scanner sc) {
        // on va s'aider de la classe GrepReader pour lire le fichier ligne par ligne
        // le fichier est de la forme :
        // argument(A).
        // argument(B).
        // argument(C).
        // contradiction(A,B).
        // contradiction(B,C).

        arguments = new ArrayList<Argument>();

        File file;
        while (true) {
            try {
                String filename = sc.nextLine();
                file = new File(filename);
                GrepReader reader = new GrepReader(new FileReader(file));
                while (true) {
                    String line = reader.readLine("argument(");
                    if (line == null) {
                        break;
                    }
                    String nom = line.substring(9, line.length() - 2);
                    arguments.add(new Argument(nom));
                }
                // maintenant on a tous les arguments
                graphe = new boolean[arguments.size()][arguments.size()];
                for (int i = 0; i < arguments.size(); i++) {
                    for (int j = 0; j < arguments.size(); j++) {
                        graphe[i][j] = false;
                    }
                }

                // on passe aux contradictions
                reader = new GrepReader(new FileReader(file));
                while (true) {
                    String line = reader.readLine("contradiction(");
                    if (line == null) {
                        break;
                    }
                    String nom1 = line.substring(14, line.indexOf(","));
                    String nom2 = line.substring(line.indexOf(",") + 1, line.length() - 2);
                    ajouterContradiction(nom1, nom2);
                }
                break;

            } catch (Exception e) {
                // le fichier n'existe pas
                System.out.println("[ERREUR] Le fichier n'existe pas");
                System.out.println("Entrez le nom du fichier contenant le graphe :");
            }
        }

    }

    /**
     * Ajoute une contradiction entre deux arguments
     *
     * @param arg1 le premier argument
     * @param arg2 le deuxieme argument
     */
    public void ajouterContradiction(String arg1, String arg2) {
        int i = 0;
        int j = 0;
        boolean trouve = false;
        while (i < arguments.size() && !trouve) {
            if (arguments.get(i).getNom().equals(arg1)) {
                trouve = true;
            } else {
                i++;
            }
        }
        trouve = false;
        while (j < arguments.size() && !trouve) {
            if (arguments.get(j).getNom().equals(arg2)) {
                trouve = true;
            } else {
                j++;
            }
        }
        if (i == arguments.size() || j == arguments.size()) {
            System.out.println("Erreur : argument non trouvé");
        } else {
            graphe[i][j] = true;
        }
    }

    public Argument getArgument(String nom) {
        for (Argument arg : arguments) {
            if (arg.getNom().equals(nom)) {
                return arg;
            }
        }
        return null;
    }

    /**
     * Verifie si les arguments sont en contradiction
     *
     * @param argument  le premier argument
     * @param argument1 le deuxieme argument
     * @return true si les arguments sont en contradiction, false sinon
     */
    public boolean estContradiction(Argument argument, Argument argument1) {
        int i = 0;
        int j = 0;
        boolean trouve = false;
        while (i < arguments.size() && !trouve) {
            if (arguments.get(i).equals(argument)) {
                trouve = true;
            } else {
                i++;
            }
        }
        trouve = false;
        while (j < arguments.size() && !trouve) {
            if (arguments.get(j).equals(argument1)) {
                trouve = true;
            } else {
                j++;
            }
        }
        return graphe[i][j];
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }
}