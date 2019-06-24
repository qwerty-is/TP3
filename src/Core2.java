public class Core2 implements Tarea {
    private Monitor monitor;

    public Core2(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(){
        monitor.disparar(8);
        monitor.disparar(2);
        while (!monitor.disparar(9))
        if (monitor.puedoDisparar(7)){
            monitor.disparar(7);
            return true;
        }
        return false;
    }
}
