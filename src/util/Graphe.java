package util;

import java.util.ArrayList;

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
        graphe[i][j] = true;
        graphe[j][i] = true;
    }

    public Argument getArgument(String nom) {
        for (Argument arg : arguments) {
            if (arg.getNom().equals(nom)) {
                return arg;
            }
        }
        return null;
    }

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