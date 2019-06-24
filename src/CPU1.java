
public class CPU1 extends Nucleo {
    private Monitor monitor;
    private int[] transiciones={0,0,0,0};

    public CPU1(int repeticiones, Monitor monitor){
        super(repeticiones, monitor);
        super.setTransiciones(transiciones);
    }
}
