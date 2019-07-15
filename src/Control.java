import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class Control extends Thread{

    private Procesador procesador;
    private String[] mensaje;
    private String estado;
    private final static Logger logger = Logger.getLogger("ControlBuffer");

    public Control(Procesador procesador, String estado) {

        mensaje=new String[2];
        this.estado=estado;
        this.procesador=procesador;

        try {
            Handler fileHandler = new FileHandler("C:\\Users\\nicoc\\IdeaProjects\\TP3\\MyLog.log", true);

            SimpleFormatter simpleFormatter = new SimpleFormatter();

            fileHandler.setFormatter(simpleFormatter);

            logger.addHandler(fileHandler);

            fileHandler.setLevel(Level.ALL);

        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }


    }


    @Override
    public void run() {
        long tdeinicio=System.currentTimeMillis();
        logger.log(Level.INFO, "INICIO");
        while(estado.equals("Ejecucion")) {
            mensaje=procesador.getStats();
            String stats = mensaje[0];
            estado=mensaje[1];

            logger.log(Level.INFO, stats);

            if (estado.equals("Ejecucion")){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.log(Level.INFO, "FIN");
        logger.log(Level.INFO, ("\nTiempo de ejecucion: " + (System.currentTimeMillis()-tdeinicio)));
    }


}