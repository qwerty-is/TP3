
public class CPU1 extends Nucleo {
    private int[] transiciones={0,1,5,2,6,7};

    public CPU1(int repeticiones, Buffer miBuffer, Monitor monitor){
        super(repeticiones, miBuffer, monitor);
        super.setTransiciones(transiciones);
    }
}
