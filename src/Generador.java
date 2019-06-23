
public class Generador implements Runnable{
    private int repeticiones;
    private int T;
    private Monitor monitor;

    public Generador(int repeticiones, Monitor monitor){
        this.repeticiones=repeticiones;
        this.monitor=monitor;
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            T=3;
            monitor.disparar(T);


        }
    }

}
