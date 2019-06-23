
import java.util.BitSet;

public class RDP {

    private final int FILAS=10;
    private final int COLUMNAS=8;
    private int[][] marcadoActual={{0},{0},{8},{0},{0},{10},{15},{0},{0},{5}};
    private int[][] matrizW={

            {0,	0,	0,	1,	0,	0,	0,	-1},
            {0,	0,	0,	0,	1,	0,	-1,	0},
            {0,	0,	0,	-1,	-1,	0,	1,	1},
            {1,	0,	0,	-1,	0,	0,	0,	0},
            {0,	0,	0,	0,	-1,	1,	0,	0},
            {0,	-1,	0,	0,	0,	0,	0,	1},
            {0,	0,	-1,	0,	0,	0,	1,	0},
            {-1,1,	0,	0,	0,	0,	0,	0},
            {0,	0,	1,	0,	0,	-1,	0,	0},
            {1,	-1,	-1,	0,	0,	1,	0,	0},

    };
    private BitSet Sensibilizadas;

    public RDP(){
        Sensibilizadas=new BitSet(COLUMNAS);
        Sensibilizadas.set(1);
        Sensibilizadas.set(2);
    }

    public BitSet getSensibilizadas() {
        return Sensibilizadas;
    }

    public boolean dispararRed(int transicion){
        if(puedoDisparar(transicion)){
            actualizarMarcado(transicion);
            actualizarSensibilizadas();
            return true;
        }
        return false;
    }

    private void actualizarSensibilizadas(){
        Sensibilizadas.clear();
        for(int j=0;j<COLUMNAS;j++){
            for(int i=0;i<FILAS;i++){
                if(marcadoActual[i][0]+matrizW[i][j]<0){
                    break;
                }
                if(i==FILAS-1){
                    Sensibilizadas.set(j);
                }
            }
        }
    }

    public boolean puedoDisparar(int transicion){
        return Sensibilizadas.get(transicion);
    }

    private void actualizarMarcado(int T){ marcadoActual=suma(marcadoActual,obtenerColumna(matrizW,T));
    }

    private int[][] obtenerColumna(int[][] matriz,int columna){
        int[][] column=new int[FILAS][1];
        for(int i=0;i<FILAS;i++){
            column[i][0]=matriz[i][columna];
        }
        return column;
    }

    private int[][] suma(int[][] vec1, int[][] vec2){
        int[][] resultado = new int[FILAS][1];
        for(int i=0;i<FILAS;i++){
            resultado[i][0]=vec1[i][0]+vec2[i][0];
        }
        return resultado;
    }

}