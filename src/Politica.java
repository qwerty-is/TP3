import java.util.BitSet;

public class Politica {
    private final double PROBABILIDAD=0.5;
    private final int TRANSICIONES=15;
    private final int buffer_CPU1=4;
    private final int buffer_CPU2=10;
    private int[] prioridades;

    public Politica(){
        prioridades= new int[]{5, 8, 4, 10, 3, 6, 9, 0, 12, 1, 13, 2, 11, 7, 14};
    }

    public int dondeGuardo(){
        if (Math.random()<=PROBABILIDAD){
            return buffer_CPU1;
        }
        return buffer_CPU2;
    }

    public int cualDespierto(BitSet opciones){
        for(int i=0; i<TRANSICIONES-1; i++){
            if(opciones.get(prioridades[i])){
                return prioridades[i];
            }
        }
        return prioridades[TRANSICIONES-1];
    }
}
