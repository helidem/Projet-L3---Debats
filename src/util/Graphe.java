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
                        if (arguments.size() == 0) {
                            throw new ExceptionFichier("[ERREUR] Le fichier ne contient pas d'arguments");
                        }
                        break;
                    }
                    String nom = line.substring(9, line.length() - 2);
                    if (!verifierArgument(nom))
                        throw new ExceptionFichier("[ERREUR] Probleme dans le fichier, veuillez verifier que les arguments sont bien ecrits");
                    arguments.add(new Argument(nom));
                }

                listeContradictions = new HashMap<>();


                reader = new GrepReader(new FileReader(file));
                while (true) {
                    String line = reader.readLine("contradiction(");
                    if (line == null) {
                        if (listeContradictions.size() == 0) {
                            throw new ExceptionFichier("[ERREUR] Le fichier ne contient pas de contradictions");
                        }
                        break;
                    }
                    String nom1 = null;
                    String nom2 = null;
                    try {
                        nom1 = line.substring(14, line.indexOf(","));
                        nom2 = line.substring(line.indexOf(",") + 1, line.length() - 2);
                    } catch (Exception e) {
                        throw new ExceptionFichier("[ERREUR] Probleme dans le fichier, veuillez verifier que les contradictions sont bien ecrites");
                    }
                    // verifier si les arguments existent aussi
                    if (!verifierArgument(nom1) || !verifierArgument(nom2) || nom1.equals(nom2) || !argumentEstDansGraphe(nom1) || !argumentEstDansGraphe(nom2)){
                        throw new ExceptionFichier("[ERREUR] Probleme dans le fichier, veuillez verifier que les contradictions sont bien ecrites");
                    }
                    for (Argument arg : arguments) {
                        listeContradictions.put(arg, new ArrayList<>());
                    }
                    ajouterContradiction(nom1, nom2);
                }
                break;

            } catch (FileNotFoundException e) {
                // le fichier n'existe pas
                System.out.println("[ERREUR] Le fichier n'existe pas");
                System.out.println("Entrez le nom du fichier contenant le graphe :");
            } catch (ExceptionFichier e) {
                System.out.println("Entrez le nom du fichier contenant le graphe :");
                this.listeContradictions = new HashMap<>();
                this.arguments = new ArrayList<>();
            }
        }
    }

    private boolean argumentEstDansGraphe(String nom1) {
        for(Argument arg : arguments){
            if(arg.getNom().equals(nom1))
                return true;
        }
        return false;
    }

    private boolean verifierArgument(String nom) {
        // l'argument doit etre unique
        // l'argument ne doit pas contenir de virgule, ni de parenthese ni d'espace, ni le mot "contradiction" ni le mot "argument"
        if (nom.contains(",") || nom.contains("(") || nom.contains(")") || nom.contains(" ") || nom.contains("contradiction") || nom.contains("argument")) {
            System.out.println("Le nom de l'argument ne doit pas contenir de virgule, ni de parenthese ni d'espace, ni le mot \"contradiction\" ni le mot \"argument\"");
            return false;
        }
        return true;
    }

    /**
     * @param argument  le premier argument
     * @param argument1 le deuxieme argument
     * @brief Ajoute une contradiction entre deux arguments
     */
    public void ajouterContradiction(String argument, String argument1) {
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