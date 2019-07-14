public class Iniciar1 implements Tarea{
    @Override
    public void ejecutar(Monitor monitor) {
        monitor.disparar(0);
        monitor.disparar(1);
        monitor.disparar(7);
    }
}
