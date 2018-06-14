package michal.chabiera;

import java.util.ArrayList;
import java.util.List;

public class Wezel implements Comparable<Wezel>{

    public final int id;
    public List<Lacze> sasiedzi = new ArrayList<Lacze>();
    public int najkrotszaOdleglosc = Integer.MAX_VALUE;
    public Wezel parent;

    public Wezel(int id){
        this.id = id;
    }

    public String toString(){
        return String.valueOf(id);
    }

    public int compareTo(Wezel other){
        return Double.compare(najkrotszaOdleglosc, other.najkrotszaOdleglosc);
    }
}
