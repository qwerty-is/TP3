public class Main {

    public static void main (String args[]) {

        Procesador procesador=new Procesador();
        Control log=new Control(procesador,"Ejecucion");

        log.start();
        Thread hilos[]=procesador.getThreads();
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