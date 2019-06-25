public class Main {

    public static void main (String args[]) {

        Politica politica=new Politica();
        Monitor monitor=new Monitor(politica);
        Buffer buffer1=new Buffer();
        Buffer buffer2=new Buffer();
        Nucleo core1=new CPU1(1000, buffer1, monitor);
        Nucleo core2=new CPU2(1000, buffer2, monitor);
        Generador generador= new Generador(1000, buffer1, buffer2, monitor, politica);
        MyThreadFactory factory=new MyThreadFactory("procesador");

        Control log=new Control(monitor, buffer1, buffer2, "Ejecucion");

        Thread gen=factory.newThread(generador);
        Thread n1=factory.newThread(core1);
        Thread n2=factory.newThread(core2);
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