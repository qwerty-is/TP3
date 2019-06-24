public class Procesar implements Tarea {
    private Monitor monitor;

    public Procesar(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(int[] transiciones, boolean standBy) {
        monitor.disparar(transiciones[2]);
        if(!standBy){
            monitor.disparar(transiciones[3]);
        }
        monitor.disparar(transiciones[4]);
        if (monitor.puedoDisparar(transiciones[5])){
            monitor.disparar(transiciones[5]);
            return true;
        }
        return false;
    }
}
