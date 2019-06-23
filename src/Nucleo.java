public class Nucleo implements Runnable{
    private int repeticiones;
    private Monitor monitor;
    private int tipo;
    private Tarea tarea;

    public Nucleo(int repeticiones, Monitor monitor, int tipo){
        this.repeticiones=repeticiones;
        this.monitor=monitor;
        this.tipo=tipo;
    }

    @Override
    public void run(){
        if (tipo==1){
            tarea=new Iniciador(monitor);
        }

        for (int i=0; i<repeticiones; i++){
            if (tipo==1){
                tarea=new Core1(monitor);
            }
            else tarea=new Core2(monitor);

            if (tarea.ejecutar()){
                tarea=new Iniciador(monitor);
            }
        }
    }
}
