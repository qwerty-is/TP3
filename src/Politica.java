import java.util.BitSet;

public class Politica {
    private final double PROBABILIDAD=0.5;
    private final int TRANSICIONES=15;
    private final int buffer_CPU1=4;
    private final int buffer_CPU2=10;
    private final int T5=5;
    private final int T8=8;
    private final int T4=4;
    private final int T10=10;
    private final int Arrival_rate=3;
    private final int Service_rate_1=6;
    private final int Service_rate_2=9;
    private final int T0=0;
    private final int T12=12;
    private final int Power_Up_Delay_1=1;
    private final int Power_Up_Delay_2=13;
    private final int T2=2;
    private final int T11=11;
    private final int Power_Down_Threshold_1=7;
    private final int Power_Down_Threshold_2=14;
    private int[] prioridades;

    public Politica(){
        prioridades= new int[]{T5, T8, T4, T10, Arrival_rate, Service_rate_1, Service_rate_2, T0, T12, Power_Up_Delay_1, Power_Up_Delay_2, T2, T11, Power_Down_Threshold_1, Power_Down_Threshold_2};
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
