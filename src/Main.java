public class Main {

    public static void main (String args[]) {

        Procesador procesador=new Procesador();
        Thread hilos[]=procesador.getThreads();
        Control log=new Control(procesador,"Ejecucion");

        log.start();
        for(int i=0;i<procesador.getCANTIDADHILOS();i++){
            hilos[i].start();
        }

        try {
            log.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}