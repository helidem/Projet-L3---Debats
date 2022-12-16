/**
 * @file src/util/Graphe.java
 * @brief Classe Graphe qui represente un graphe
 * @author Youcef MEDILEH
 * @version 1.0
 */

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

        arguments = new ArrayList<>();

        initGrapheFile(sc);
    }

    /**
     * @param sc le scanner
     * @brief Initialise le graphe a partir d'un fichier
     */
    private void initGrapheFile(Scanner sc) {
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

                listeContradictions = new HashMap<>();
                for (Argument arg : arguments) {
                    listeContradictions.put(arg, new ArrayList<>());
                }

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
     * @param arg1 le premier argument
     * @param arg2 le deuxieme argument
     * @brief Ajoute une contradiction entre deux arguments
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
     * @param A le premier argument
     * @param B le deuxieme argument
     * @return true si les arguments sont en contradiction, false sinon
     * @brief Verifie si les arguments sont en contradiction
     */
    public boolean AcontreditB(Argument A, Argument B) {
        return listeContradictions.get(A).contains(B);
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }
}