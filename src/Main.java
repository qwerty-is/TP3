public class Main {

    public static void main (String args[]) {

        Politica politica=new Politica();
        Monitor monitor=new Monitor(politica);
        Nucleo nucleo1=new Nucleo(1000, monitor, 1, true);
        Nucleo nucleo2=new Nucleo(1000, monitor, 2, false);
        Generador generador= new Generador(1000, monitor, politica);
        MyThreadFactory factory=new MyThreadFactory("procesador");

        Control log=new Control(monitor,"Ejecucion");

        Thread gen=factory.newThread(generador);
        Thread n1=factory.newThread(nucleo1);
        Thread n2=factory.newThread(nucleo2);
        Thread con=factory.newThread(log);

        con.start();
        gen.start();
        n1.start();
        n2.start();

        try {
            gen.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.setEstado("Fin");

    }

}