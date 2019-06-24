public class Iniciar implements Tarea {
    private Monitor monitor;

    public Iniciar(Monitor monitor){
        this.monitor=monitor;
    }

    @Override
    public boolean ejecutar(){
        monitor.disparar(0);
        monitor.disparar(1);
        return true;
    }
}
