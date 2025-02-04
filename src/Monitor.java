import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Monitor {
    private final int TRANSICIONES=15;
    private Semaphore semaphore;
    private List<Semaphore> colas;
    private RDP rdp;
    private Politica miPolitica;
    private int Tareas_Nucleo_2;
    private int Tareas_Nucleo_1;
    private BitSet opciones;
    private boolean k;

    public Monitor(Politica politica){
        semaphore=new Semaphore(1,true);
        colas=new ArrayList<>();
        for(int i=0; i<TRANSICIONES; i++){
            colas.add(new Semaphore(0, true));
        }
        rdp=new RDP();
        opciones=new BitSet(TRANSICIONES);
        miPolitica=politica;
        Tareas_Nucleo_1=0;
        Tareas_Nucleo_2=0;
    }

    public boolean disparar(int T){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        k=true;
        while(k==true){

            k=rdp.puedoDisparar(T);

            if(k==true){
                if (rdp.inVentana(T)){
                    rdp.dispararRed(T);

                    switch(T){
                        case 4: Tareas_Nucleo_1++;   break;
                        case 5: Tareas_Nucleo_1--;   break;
                        case 8: Tareas_Nucleo_2++;   break;
                        case 10: Tareas_Nucleo_2--;   break;
                        default:                        break;
                    }

                    if(hayHilos()){
                        colas.get(miPolitica.cualDespierto(opciones)).release();
                        return true;
                    }

                    k=false;
                }
                else {
                    semaphore.release();
                    try {
                        Thread.currentThread().sleep(rdp.tiempoRestante(T));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
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
        return true;
    }

    public boolean puedoDisparar(int T){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(rdp.puedoDisparar(T)){
            rdp.dispararRed(T);
            if (hayHilos()){
                colas.get(miPolitica.cualDespierto(opciones)).release();
            }
            else { semaphore.release();}
            return true;
        }
        semaphore.release();
        return false;

    }

    private boolean hayHilos(){
        if(largoColas()==0){
            return false;
        }
        BitSet sensibilizadas=rdp.getSensibilizadas();
        opciones.clear();
        for(int i=0; i<TRANSICIONES; i++){
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
        for(int i=0; i<TRANSICIONES; i++){
            ret=ret+colas.get(i).getQueueLength();
        }
        return ret;
    }


    public String estadisticas (){
        String stats;
        stats="\nTareas en espera del nucleo1: " + Tareas_Nucleo_1 + "\nTareas en espera del nucleo2: " + Tareas_Nucleo_2
                + "\nHilos en las colas de espera: " + largoColas()
                + "\nHilos en espera del mutex: " + semaphore.getQueueLength();
        return stats;

    }

}
