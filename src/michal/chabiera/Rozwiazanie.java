package michal.chabiera;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Rozwiazanie {
    private int iloscWezlow;
    private int iloscLaczy;
    private List<Wezel> listaWezlow = new ArrayList<Wezel>();
    private List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    private List<Lacze> listaLaczy = new ArrayList<Lacze>();
    List<Wezel> sciezka = new ArrayList<Wezel>();

    public Rozwiazanie() throws FileNotFoundException {
        wczytajDane();
        generujZapotrzebowania();

        for(Zapotrzebowanie z : listaZapotrzebowan){
            liczLambdy(z);
        }

    }
    private void liczLambdy(Zapotrzebowanie z) {
        List<Lacze> sciezkaJakoLacza = new ArrayList<>();
        obliczSciezke(z.poczatek);
        sciezka = getNajkrotszaSciezkaDo(z.koniec);

        z.lacza = dodajLaczaDoZapotrzebowania(sciezka);

        int dlugosc_lambda = znajdzDlugoscLambda(z.lacza);
        z.setLambda(dlugosc_lambda+1);
        przydzielLambdeSciezce(z.lacza ,dlugosc_lambda+1);
    }
    private int znajdzDlugoscLambda(List<Integer> l) { //przekazuje numery laczy skladajacych sie na sciezke
        List<Integer> listaDlugosciFal = new ArrayList<>();
        int lambdazapamietana = znajdzMaxZapamietana(l);
        if(lambdazapamietana == 0){
            return lambdazapamietana;
        }
        //zsumowac wszytkie!
        for(int i = 0; i < l.size(); i++){
            for(Integer lambda : listaLaczy.get(l.get(i)).listaLambd ){
                listaDlugosciFal.add(lambda);
            }

            //listaDlugosciFal.addAll();
        }
        Collections.sort(listaDlugosciFal);
        //usunąć powtorzenia
        usunPowtorzenia(listaDlugosciFal);

        if(lambdazapamietana == listaDlugosciFal.size()){
            return lambdazapamietana;
        }
        if(lambdazapamietana > listaDlugosciFal.size()){
            for(int i = 1; i <=lambdazapamietana; i++){
                if(!listaDlugosciFal.contains(lambdazapamietana-i)){
                    return lambdazapamietana - i - 1;
                }
            }
        }
        if(lambdazapamietana < listaDlugosciFal.size()){
            return listaDlugosciFal.get(listaDlugosciFal.size()-1);
        }
        return -1;
    }
    private int znajdzMaxZapamietana(List<Integer> l) {
        int max = listaLaczy.get(l.get(0)).getLambda(); // lacze 3 ma indeks 2 w liscie laczy
        for(int i = 1; i < l.size(); i++){
            int tmp = listaLaczy.get(l.get(i)).getLambda(); // lacze 3 ma indeks 2 w liscie laczy
            if(tmp > max){
                max = tmp;
            } else {
                tmp = 0;
            }
        }
        return max;
    }
    private void przydzielLambdeSciezce(List<Integer> l, int max) {
        for(Integer i : l){
            przypiszLambdeLaczu(i, max);
        }
    }
    private void przypiszLambdeLaczu(Integer i, int max) {
        listaLaczy.get(i).setLambda(max);
    }
    //usuwa powtarzajace sie inty w liscie
    public void usunPowtorzenia(List<Integer> lista){
        int size = lista.size();
        for(int i = 0 ;i < size ; i++){
            for(int j=1; j<size; j++){
                if(i!=j && lista.get(i)==lista.get(j)){
                    lista.remove(j);
                    size = lista.size();
                }
            }
        }
    }

    private List<Integer> dodajLaczaDoZapotrzebowania(List<Wezel> sciezka) {
        List<Integer> tmp = new ArrayList<Integer>();
        for(int i = 0 ; i < sciezka.size()-1; i++){
            int pocz = sciezka.get(i).id;
            int kon = sciezka.get(i+1).id;
            tmp.add(listaLaczy.stream().filter(
                    x -> (x.koniec.id == pocz && x.poczatek.id == kon) || (x.koniec.id == kon && x.poczatek.id == pocz)).
                    findFirst().get().id);
        }
        return tmp;
    }





    //DIJKSTRA
    public static void obliczSciezke(Wezel poczatek){
        poczatek.najkrotszaOdleglosc = 0;
        //implement a priority nieodwiedzoneWezly
        PriorityQueue<Wezel> nieodwiedzoneWezly = new PriorityQueue<Wezel>();
        nieodwiedzoneWezly.add(poczatek);

        while(!nieodwiedzoneWezly.isEmpty()){
            Wezel rozpatrywanyWezel = nieodwiedzoneWezly.poll();
			/*visit the adjacencies, starting from
			the nearest node(smallest najkrotszaOdleglosc)*/
            for(Lacze l: rozpatrywanyWezel.sasiedzi){
                Wezel v = l.koniec;
                int waga = l.waga;
                //relax(rozpatrywanyWezel,v,waga)
                int odlegloscOdRozpatrywanegoWezla = rozpatrywanyWezel.najkrotszaOdleglosc +waga;
                if(odlegloscOdRozpatrywanegoWezla < v.najkrotszaOdleglosc){
					/*remove v from nieodwiedzoneWezly for updating
					the najkrotszaOdleglosc value*/
                    nieodwiedzoneWezly.remove(v);
                    v.najkrotszaOdleglosc = odlegloscOdRozpatrywanegoWezla;
                    v.parent = rozpatrywanyWezel;
                    nieodwiedzoneWezly.add(v);
                }
            }
        }
    }
    public static List<Wezel> getNajkrotszaSciezkaDo(Wezel target){
        //trace path from target to source
        List<Wezel> path = new ArrayList<Wezel>();
        for(Wezel node = target; node!=null; node = node.parent){
            path.add(node);
        }
        //reverse the order such that it will be from source to target
        Collections.reverse(path);

        return path;
    }

    //WCZYTYWANIE
    public void wczytajDane() throws FileNotFoundException {
        File file = new File("resources/siec.txt");
        System.out.println(file.getAbsolutePath());
        Scanner scanner = new Scanner(file);

        wczytajWezly(scanner);
        wczytajLacza(scanner);
    }
    private void wczytajLacza(Scanner scanner) {
        iloscLaczy = Integer.parseInt(scanner.nextLine());

        for(int i = 0; i < iloscLaczy; i++) {
            String linia;
            String[] liniaLista;
            linia = scanner.nextLine();
            liniaLista = linia.split(" ");
            int poczatek = Integer.parseInt((liniaLista[0]));
            int koniec = Integer.parseInt((liniaLista[1]));
            int odleglosc = Integer.parseInt((liniaLista[2]));
            Lacze lacze = new Lacze(i, listaWezlow.get(poczatek), listaWezlow.get(koniec), odleglosc);

            listaWezlow.get(poczatek).sasiedzi.add(lacze);
            listaWezlow.get(koniec).sasiedzi.add(lacze);

            listaLaczy.add(lacze);
        }
    }
    private void wczytajWezly(Scanner scanner) {
        iloscWezlow = Integer.parseInt(scanner.nextLine());

        for(int i = 0; i < iloscWezlow; i++){
            listaWezlow.add(new Wezel(i));
        }
    }
    private void generujZapotrzebowania(){
        int id = 0;
        for(int i = 0; i < iloscWezlow; i++){
            for(int j = 0; j< iloscWezlow; j++){
                if(i != j && i < j){
                    Zapotrzebowanie zapotrzebowanie = new Zapotrzebowanie(id, listaWezlow.get(i), listaWezlow.get(j));
                    listaZapotrzebowan.add(zapotrzebowanie);
                    id++;
                }
            }
        }
    }
}
