import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {
    private int contador;
    private List<String> stats;
    private String nombre;

    public MyThreadFactory(String nombre){
        contador=0;
        this.nombre=nombre;
        stats=new ArrayList<String>();
    }

    @Override
    public Thread newThread(Runnable r){
        Thread t=new Thread(r, nombre+"-Thread_"+contador);
        contador++;

        return t;
    }

    public String getStats(){
        StringBuffer buffer=new StringBuffer();
        Iterator<String> it=stats.iterator();

        while (it.hasNext()){
            buffer.append(it.next());
        }

        return buffer.toString();
    }
}
