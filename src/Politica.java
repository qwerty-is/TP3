import java.util.BitSet;

public class Politica {
    private int[] prioridades;

    public Politica(){
        prioridades= new int[]{5, 8, 4, 10, 3, 6, 9, 0, 1, 2, 7};
    }

    public int dondeGuardo(){
        if (Math.random()<0.5){
            return 4;
        }
        return 10;
    }

    public int cualDespierto(BitSet opciones){
        for(int i=0; i<10; i++){
            if(opciones.get(prioridades[i])){
                return prioridades[i];
            }
        }
        return prioridades[10];
    }
}
