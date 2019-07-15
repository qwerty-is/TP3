public class GarbageCollector implements Runnable{
    private int T;
    private Monitor monitor;

    GarbageCollector(int cpu, Monitor monitor){
        this.monitor=monitor;
        if (cpu==1) T=2;
        else T=11;
    }

    @Override
    public void run(){
        while(true){
            monitor.disparar(T);
        }
    }
}
