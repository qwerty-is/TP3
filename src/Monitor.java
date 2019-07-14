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
        miPolitica=politica;;
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
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String stats;
        stats="\nHilos en las colas de espera: " + largoColas()
                + "\nHilos en espera del mutex: " + semaphore.getQueueLength();
        semaphore.release();
        return stats;

    }

}
