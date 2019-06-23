public class Core2 implements Tarea {
    private Monitor monitor;

    public Core2(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(){
        monitor.disparar(9);
        monitor.disparar(10);
        if (monitor.puedoDisparar(8)){
            monitor.disparar(8);
            return true;
        }
        return false;
    }
}
