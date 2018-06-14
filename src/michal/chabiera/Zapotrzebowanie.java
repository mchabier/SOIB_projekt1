package michal.chabiera;

import java.util.ArrayList;
import java.util.List;

public class Zapotrzebowanie {
    int id;
    Wezel poczatek;
    Wezel koniec;
    int przydzielonaLambda;
    public List<Integer> lacza = new ArrayList<>();

    Zapotrzebowanie(int id, Wezel poczatek, Wezel koniec){
        this.id = id;
        this.poczatek = poczatek;
        this.koniec = koniec;
        this.przydzielonaLambda = 0;
    }
    public void setLambda(int lambda){
        if(lambda > this.przydzielonaLambda){
            this.przydzielonaLambda = lambda;
        }
    }
    public void dodajLacze(int lacze){
        lacza.add(lacze);
    }
}
