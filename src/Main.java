public class Main {

    public static void main (String args[]) {

        Politica politica=new Politica();
        Monitor monitor=new Monitor(politica);
        Nucleo core1=new CPU1(1000, monitor);
        Nucleo core2=new CPU2(1000, monitor);
        Generador generador= new Generador(1000, monitor, politica);
        MyThreadFactory factory=new MyThreadFactory("procesador");

        Control log=new Control(monitor,"Ejecucion");

        Thread gen=factory.newThread(generador);
        Thread n1=factory.newThread(core1);
        Thread n2=factory.newThread(core2);
        Thread con=factory.newThread(log);

        long tdeinicio=System.currentTimeMillis();
        con.start();
        gen.start();
        n1.start();
        n2.start();

        try {
            gen.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Tarde: " + (System.currentTimeMillis()-tdeinicio));
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.setEstado("Fin");

    }

}