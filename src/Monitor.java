import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Monitor {
    private Semaphore semaphore;
    private List<Semaphore> colas;
    private RDP rdp;
    private Politica miPolitica;
    private int Tareas_Nucleo_2;
    private int Tareas_Nucleo_1;
    private BitSet opciones;
    private boolean k;

    public Monitor(Politica politica){
        semaphore=new Semaphore(1,false);
        colas=new ArrayList<>();
        for(int i=0; i<11; i++){
            colas.add(new Semaphore(0, true));
        }
        rdp=new RDP();
        opciones=new BitSet(11);
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

            System.out.println("Hola soy: " + Thread.currentThread().getName() + " y quiero disparar " + (T+2));
            k=rdp.puedoDisparar(T);
            System.out.println("K vale: "+k);

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
                    System.out.println("Hola soy: " + Thread.currentThread().getName() + " y llegue antes de la ventana de " + (T+2));
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
                System.out.println("Hola soy: " + Thread.currentThread().getName() + " y no pude disparar " + (T+2));
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
        stats="\nTareas en espera del nucleo1: " + Tareas_Nucleo_1 + "\nTareas en espera del nucleo2: " + Tareas_Nucleo_2
                + "\nHilos en las colas de espera: " + largoColas()
                + "\nHilos en espera del mutex: " + semaphore.getQueueLength();
        return stats;

    }

}
