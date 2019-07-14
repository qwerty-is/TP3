
public class Generador implements Runnable{
    private int repeticiones;
    private int T;
    private Monitor monitor;
    private Politica politica;
    private Buffer buf1;
    private Buffer buf2;

    public Generador(int repeticiones, Monitor monitor, Politica politica, Buffer buf1, Buffer buf2){
        this.repeticiones=repeticiones;
        this.monitor=monitor;
        this.politica=politica;
        this.buf1=buf1;
        this.buf2=buf2;
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            T=3;
            while (!monitor.disparar(T)){}
            T=politica.dondeGuardo();
            monitor.disparar(T);
            if(T==4) buf1.add();
            else buf2.add();
        }
    }
}
