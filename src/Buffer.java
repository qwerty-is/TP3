import java.util.LinkedList;
import java.util.Queue;


public class Buffer extends Thread {

    private Queue<Integer> m;
    int cantidad;

    public Buffer() {
        this.m = new LinkedList<>();
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
        return m.add(e);
    }

    synchronized public boolean remove() {
        if(puedoSacar()){
            cantidad--;
            m.remove();
            return true;
        }
        return false;
    }

    synchronized public int getCantidad() {
        return cantidad;
    }
}