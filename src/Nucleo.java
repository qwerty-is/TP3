public class Nucleo implements Runnable{
    private int repeticiones;
    private Monitor monitor;
    private int tipo;
    private Tarea tarea;
    private boolean iniciar;

    public Nucleo(int repeticiones, Monitor monitor, int tipo, boolean iniciar){
        this.repeticiones=repeticiones;
        this.monitor=monitor;
        this.tipo=tipo;
        this.iniciar=iniciar;
        establecerTipo();
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            if(iniciar){
                tarea=new Iniciador(monitor);
                tarea.ejecutar();
                establecerTipo();
            }
            iniciar=tarea.ejecutar();
        }
    }

    private void establecerTipo(){
        if (tipo==1){
            tarea=new Core1(monitor);
        }
        else {
            tarea=new Core2(monitor);
        }
    }
}
