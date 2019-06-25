public class Nucleo implements Runnable{
    private int repeticiones;
    private Buffer miBuffer;
    private Monitor monitor;
    private int[] transiciones;
    private Tarea tarea;
    private boolean standBy;

    public Nucleo(int repeticiones, Buffer miBuffer, Monitor monitor){
        this.repeticiones=repeticiones;
        this.miBuffer=miBuffer;
        this.monitor=monitor;
        standBy=true;
    }

    public void setTransiciones(int[] transiciones) {
        this.transiciones = transiciones;
    }

    @Override
    public void run(){
        for (int i=0; i<repeticiones; i++){
            if (standBy){
                tarea=new Iniciar(monitor);
                tarea.ejecutar(transiciones, miBuffer, true);
                tarea=new Procesar(monitor);
            }
            standBy=tarea.ejecutar(transiciones, miBuffer, standBy);
        }

    }

}
