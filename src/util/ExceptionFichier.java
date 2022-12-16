package util;

public class ExceptionFichier extends Throwable {
    public ExceptionFichier(String problemeDansLeFichier) {
        System.out.println(problemeDansLeFichier);
    }
}
