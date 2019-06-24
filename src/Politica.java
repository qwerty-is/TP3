import java.util.BitSet;

public class Politica {
    private int[] prioridades;

    public Politica(){
        prioridades= new int[]{5, 8, 4, 10, 3, 6, 9, 0, 1, 2, 7};
    }

    public int cual(BitSet opciones){
        for(int i=0; i<10; i++){
            if(opciones.get(prioridades[i])){
                return prioridades[i];
            }
        }
        return prioridades[10];
    }
}
