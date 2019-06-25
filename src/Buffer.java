import java.util.LinkedList;
import java.util.Queue;


public class Buffer {

    private Queue<Integer> queue;
    private int cantidad;

    public Buffer() {
        this.queue = new LinkedList<>();
        this.cantidad = 0;
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
        return queue.add(e);
    }

    synchronized public boolean remove() {
        if(puedoSacar()){
            cantidad--;
            queue.remove();
            return true;
        }
        return false;
    }

    synchronized public int getCantidad() {
        return cantidad;
    }
}