import java.util.LinkedList;
import java.util.Queue;


public class Buffer {

    private Queue<Integer> queue;
    private int cantidad;
    private int actRealizadas;
    private int actGeneradas;

    public Buffer() {
        this.queue = new LinkedList<>();
        this.cantidad = 0;
        this.actRealizadas=0;
        this.actGeneradas=0;
    }

    private boolean puedoSacar() {
        if(cantidad > 0) {
            return true;
        }
        return false;
    }

    synchronized public boolean add() {
        Integer e=1;
        cantidad++;
        actGeneradas++;
        return queue.add(e);
    }

    synchronized public boolean remove() {
        if(puedoSacar()){
            cantidad--;
            queue.remove();
            actRealizadas++;
            return true;
        }
        return false;
    }

    synchronized public int getActRealizadas(){
        return actRealizadas;
    }
    synchronized public int getActGeneradas(){ return actGeneradas; }
}