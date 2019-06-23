public class Core1 implements Tarea {
    private Monitor monitor;

    public Core1(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(){
        monitor.disparar(5);
        monitor.disparar(6);
        if (monitor.puedoDisparar(8)){
            monitor.disparar(8);
            return true;
        }
        return false;
    }
}
