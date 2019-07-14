public class Procesador {

    private final int CANTIDADHILOS=7;
    private final int REPETICIONES=1000;
    private Politica miPolitica;
    private Monitor monitor;
    private Generador gen;
    private Buffer buf1;
    private Buffer buf2;
    private CPU cpu1;
    private CPU cpu2;
    private GarbageCollector collector1;
    private GarbageCollector collector2;
    private Iniciador iniciador1;
    private Iniciador iniciador2;
    private Thread threads[];
    private MyThreadFactory factory;

    public Procesador (){
        threads=new Thread[CANTIDADHILOS];
        factory=new MyThreadFactory("procesador");
        miPolitica=new Politica();
        monitor=new Monitor(miPolitica);
        buf1=new Buffer();
        buf2=new Buffer();
        gen=new Generador(REPETICIONES, monitor, miPolitica, buf1, buf2);
        cpu1=new CPU(monitor, buf1, 1);
        cpu2=new CPU(monitor, buf2, 2);
        iniciador1=new Iniciador(monitor,1);
        iniciador2=new Iniciador(monitor,2);
        collector1=new GarbageCollector(1, monitor);
        collector2=new GarbageCollector(2, monitor);


        crearHilos();
    }

    private void crearHilos(){
        threads[0]=factory.newThread(gen);
        threads[1]=factory.newThread(cpu1);
        threads[2]=factory.newThread(cpu2);
        threads[3]=factory.newThread(iniciador1);
        threads[4]=factory.newThread(iniciador2);
        threads[5]=factory.newThread(collector1);
        threads[6]=factory.newThread(collector2);


    }

    public int getCANTIDADHILOS() {
        return CANTIDADHILOS;
    }

    public Thread[] getThreads(){
        return threads;
    }

    public String[] getStats(){
        String stats;
        String estado="Ejecucion";
        String[] retorno=new String[2];

        stats=/*monitor.estadisticas() +*/ "\nActividades generadas= " + (buf1.getActGeneradas()+buf2.getActGeneradas())
                +"\nActividades realizadas cpu1= " + buf1.getActRealizadas()
                +"\nActividades realizadas cpu2= " + buf2.getActRealizadas();

        int realizadas=buf1.getActRealizadas()+ buf2.getActRealizadas();

        if (realizadas==REPETICIONES) estado="Fin";

        retorno[0]=stats;
        retorno[1]=estado;

        return retorno;
    }
}
