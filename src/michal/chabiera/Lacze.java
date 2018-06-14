package michal.chabiera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lacze {
    public int id;
    public final Wezel koniec;
    public final Wezel poczatek;
    public final int waga;
    public List<Integer> listaLambd = new ArrayList<Integer>();
    int lambda;

    public Lacze(int id, Wezel poczatek, Wezel koniec, int waga){
        this.id = id;
        this.poczatek = poczatek;
        this.koniec = koniec;
        this.waga = waga;
    }

    public void setLambda(int lambda) {
        if(lambda > this.lambda){
            this.lambda = lambda;
        }
        listaLambd.add(lambda);
    }
    public int getLambda() {
        Collections.sort(listaLambd);
        if(this.lambda == listaLambd.size()){
            return this.lambda;
        }
        if(this.lambda > listaLambd.size()){
            for(int i = 1; i <=this.lambda; i++){
                if(!listaLambd.contains(this.lambda-i)){
                    return this.lambda - i - 1;
                }
            }
        }
        return -1;
    }
}
