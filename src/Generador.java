
public class Generador implements Runnable{
    private int repeticiones;
    private int T;
    private Monitor monitor;
    private Politica politica;

    public Generador(int repeticiones, Monitor monitor, Politica politica){
        this.repeticiones=repeticiones;
        this.monitor=monitor;
        this.politica=politica;
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            T=3;
            while (!monitor.disparar(T))
            T=politica.dondeGuardo();
            monitor.disparar(T);
        }
    }
}
