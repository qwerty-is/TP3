import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class Control implements Runnable{

    private Monitor miMonitor;
    private String mensaje;
    private String estado;
    private Buffer buffer1;
    private Buffer buffer2;
    private final static Logger logger = Logger.getLogger("ControlBuffer");

    public Control(Monitor m, Buffer buffer1, Buffer buffer2, String estado) {
        miMonitor=m;
        this.buffer1=buffer1;
        this.buffer2=buffer2;
        this.estado=estado;

        try {
            Handler fileHandler = new FileHandler("C:\\Users\\Nico\\Documents\\Programación Concurrente\\TP1\\TP1 Concurrente\\MyLog.log", true);

            SimpleFormatter simpleFormatter = new SimpleFormatter();

            fileHandler.setFormatter(simpleFormatter);

            logger.addHandler(fileHandler);

            fileHandler.setLevel(Level.ALL);

        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }


    }

    private void escribir() {
        mensaje=miMonitor.estadisticas();
        mensaje="\nActividades pendientes CPU1: "+buffer1.getCantidad()
                +"\nActividades pendientes CPU2: "+buffer2.getCantidad()
                + mensaje;
        logger.log(Level.INFO, mensaje);

        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "INICIO");
        while(estado.equals("Ejecucion")) {
            escribir();
        }
        logger.log(Level.INFO, "FIN");
    }


}