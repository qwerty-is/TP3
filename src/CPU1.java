
public class CPU1 extends Nucleo {
    private int[] transiciones={0,1,5,2,6,7};

    public CPU1(int repeticiones, Monitor monitor){
        super(repeticiones, monitor);
        super.setTransiciones(transiciones);
    }
}
