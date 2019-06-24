import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Monitor {
    private Semaphore semaphore;
    private List<Semaphore> colas;
    private RDP rdp;
    private Politica miPolitica;
    private int Lugares_Vacios_1;
    private int Lugares_Vacios_2;
    private int Lugares_Ocupados_1;
    private int Lugares_Ocupados_2;
    private BitSet opciones;
    private boolean k;

    public Monitor(Politica politica){
        semaphore=new Semaphore(1,true);
        colas=new ArrayList<>();
        for(int i=0; i<11; i++){
            colas.add(new Semaphore(0, true));
        }
        rdp=new RDP();
        opciones=new BitSet(11);
        miPolitica=politica;
        Lugares_Vacios_1=10;
        Lugares_Vacios_2=15;
        Lugares_Ocupados_1=0;
        Lugares_Ocupados_2=0;
    }

    public void disparar(int T){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        k=true;
        while(k==true){

            k=rdp.dispararRed(T);

            if(k==true){

                switch(T){
                    case 0: Lugares_Ocupados_1++;   break;
                    case 1: Lugares_Vacios_1--;     break;
                    case 2: Lugares_Vacios_2--;     break;
                    case 3: Lugares_Ocupados_1--;   break;
                    case 4: Lugares_Ocupados_2--;   break;
                    case 5: Lugares_Ocupados_2++;   break;
                    case 6: Lugares_Vacios_2++;     break;
                    case 7: Lugares_Vacios_1++;     break;
                    default:                        break;
                }

                if(hayHilos()){

                    colas.get(miPolitica.cual(opciones)).release();
                    return;
                }
                k=false;

            }
            else{
                semaphore.release();
                try {
                    colas.get(T).acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        semaphore.release();
        return;
    }

    public boolean puedoDisparar(int T){
        return rdp.puedoDisparar(T);
    }

    private boolean hayHilos(){
        if(largoColas()==0){
            return false;
        }
        BitSet sensibilizadas=rdp.getSensibilizadas();
        opciones.clear();
        for(int i=0; i<11; i++){
            if(sensibilizadas.get(i)&& colas.get(i).hasQueuedThreads()){
                opciones.set(i);
            }
        }
        if (!opciones.isEmpty()){
            return true;
        }
        return false;
    }

    private int largoColas(){
        int ret=0;
        for(int i=0; i<8; i++){
            ret=ret+colas.get(i).getQueueLength();
        }
        return ret;
    }


    public String estadisticas (){
        String stats;
        stats="\nLugares ocupados buffer1: " + Lugares_Ocupados_1 + "\nLugares ocupados buffer2: " + Lugares_Ocupados_2
                + "\nHilos en las colas de espera: " + largoColas()
                + "\nHilos en espera del mutex: " + semaphore.getQueueLength();
        return stats;

    }

}
