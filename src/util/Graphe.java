package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Graphe {

    private ArrayList<Argument> arguments;
    private HashMap<Argument, ArrayList<Argument>> listeContradictions; // liste des contradictions de chaque argument

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
                // on va ajouter tout les arguments dans la liste des contradictions en tant que cle
                listeContradictions = new HashMap<Argument, ArrayList<Argument>>();
                for (Argument arg : arguments) {
                    listeContradictions.put(arg, new ArrayList<Argument>());
                }
                // maintenant on a tous les arguments
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

            } catch (FileNotFoundException e) {
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
        ajouterContradictionDansListeContradictions(arg1, arg2);
    }

    private void ajouterContradictionDansListeContradictions(String argument, String argument1) {
        for (Argument arg : listeContradictions.keySet()) {
            if (arg.getNom().equals(argument)) {
                for (Argument arg1 : arguments) {
                    if (arg1.getNom().equals(argument1)) {
                        listeContradictions.get(arg).add(arg1);
                    }
                }
            }
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
     * @param A  le premier argument
     * @param B le deuxieme argument
     * @return true si les arguments sont en contradiction, false sinon
     */
    public boolean AcontreditB(Argument A, Argument B) {
        return listeContradictions.get(A).contains(B);
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public HashMap<Argument, ArrayList<Argument>> getListeContradictions() {
        return listeContradictions;
    }
}