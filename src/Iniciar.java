public class Iniciar implements Tarea {
    private Monitor monitor;

    public Iniciar(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(int[] transiciones, boolean standBy){
        monitor.disparar(transiciones[0]);
        monitor.disparar(transiciones[1]);
        return true;
    }
}
