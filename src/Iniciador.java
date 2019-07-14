public class Iniciador implements Runnable {
    private Monitor monitor;
    private Tarea tarea;

    public Iniciador(Monitor monitor, int cpu){
        this.monitor=monitor;
        if (cpu==1) tarea=new Iniciar1();
        else tarea=new Iniciar2();
    }

    @Override
    public void run() {
        while (true){
            tarea.ejecutar(monitor);
        }
    }
}
