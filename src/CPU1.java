
public class CPU1 implements Runnable {
    private Monitor monitor;
    private Buffer miBuffer;
    private final int T1=5;
    private final int T2=6;

    public CPU1(Monitor monitor, Buffer miBuffer){
        this.monitor=monitor;
        this.miBuffer=miBuffer;
    }

    public void run(){
        while (true){
            monitor.disparar(T1);
            miBuffer.remove();
            while (!monitor.disparar(T2)){}
        }
    }
}
