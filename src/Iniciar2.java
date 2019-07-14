public class Iniciar2 implements Tarea {
    @Override
    public void ejecutar(Monitor monitor) {
        monitor.disparar(12);
        monitor.disparar(13);
        monitor.disparar(14);
    }
}
