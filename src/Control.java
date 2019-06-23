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
    private final static Logger logger = Logger.getLogger("ControlBuffer");

    public Control(Monitor m, String estado) {
        this.estado=estado;
        miMonitor=m;

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
        logger.log(Level.INFO, mensaje);

        try {
            Thread.currentThread().sleep(2000);
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