
public class Generador implements Runnable{
    private int repeticiones;
    private int T;
    private Buffer buffer1;
    private Buffer buffer2;
    private Monitor monitor;
    private Politica politica;

    public Generador(int repeticiones, Buffer buffer1, Buffer buffer2, Monitor monitor, Politica politica){
        this.repeticiones=repeticiones;
        this.buffer1=buffer1;
        this.buffer2=buffer2;
        this.monitor=monitor;
        this.politica=politica;
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            T=3;
            while (!monitor.disparar(T)){}
            T=politica.dondeGuardo();
            if (T==4){
                buffer1.add();
            }
            else {
                buffer2.add();
            }
            monitor.disparar(T);
        }
    }
}
