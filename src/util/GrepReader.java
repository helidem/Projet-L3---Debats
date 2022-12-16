package util;

import java.io.BufferedReader;
import java.io.Reader;

/**
 * Cette classe permet de lire un fichier ligne par ligne. Elle permet de trier selon un motif.
 */
public class GrepReader extends BufferedReader {

    public GrepReader(Reader in) {
        super(in);
    }

    public String readLine(String pattern) {
        String line = null;
        try {
            line = super.readLine();
            while (line != null && !line.contains(pattern)) {
                line = super.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}