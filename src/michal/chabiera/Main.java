package michal.chabiera;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        try {
            Rozwiazanie roz = new Rozwiazanie();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
