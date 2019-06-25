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
        while (!monitor.disparar(transiciones[4])){}
        return monitor.puedoDisparar(transiciones[5]);
    }
}
