
public class CPU2 implements Runnable {
    private Monitor monitor;
    private Buffer miBuffer;
    private final int T1=8;
    private final int T2=9;

    public CPU2(Monitor monitor, Buffer miBuffer){
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
