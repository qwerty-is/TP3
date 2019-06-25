import java.util.LinkedList;
import java.util.Queue;


public class Buffer extends Thread {

    private Queue<Integer> m;
    String miH = "";
    int cantidad;

    public Buffer() {
        this.m = new LinkedList<Integer>();
        this.cantidad = 0;
    }

    private boolean puedoSacar() {
        if(cantidad > 0) {
            return true;
        }
        return false;
    }
    synchronized public String getHilo() {
        return this.miH;
    }

    synchronized public boolean add() {
        Integer e=1;
        this.miH = "";
        cantidad++;
        return m.add(e);
    }

    synchronized public boolean remove() {
        if(puedoSacar()){
            this.miH = currentThread().getName();
            cantidad--;
            m.remove();
            return true;
        }
        this.miH = "";
        return false;
    }

    synchronized public int getCantidad() {
        return cantidad;
    }
}