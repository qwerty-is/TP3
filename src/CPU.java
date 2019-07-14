
public class CPU implements Runnable {
    private Monitor monitor;
    private Buffer miBuffer;
    private int T1;
    private int T2;

    public CPU(Monitor monitor, Buffer miBuffer, int tipo){
        this.monitor=monitor;
        this.miBuffer=miBuffer;
        if (tipo==1){ T1=5; T2=6;}
        else { T1=8; T2=9;}
    }

    public void run(){
        while (true){
            monitor.disparar(T1);
            miBuffer.remove();
            while (!monitor.disparar(T2)){}
        }
    }
}
