
public class CPU2 extends Nucleo {
    private int[] transiciones={12,13,8,11,9,14};

    public CPU2(int repeticiones, Monitor monitor){
        super(repeticiones, monitor);
        super.setTransiciones(transiciones);
    }

}
