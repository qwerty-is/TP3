
import java.util.BitSet;

public class RDP {

    private final int FILAS=12;
    private final int COLUMNAS=11;
    private final long ALFA=0;
    private final long BETA=0;
    private final long GAMMA=0;

    private int[][] marcadoActual={{1},{0},{0},{0},{1},{0},{0},{0},{1},{0},{0},{1}};
    private int[][] matrizW={

            {-1, 0,	0,	0,	0,	0,	0,	1,	0,	0,	0,},
            {1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,},
            {0,	-1,	-1,	0,	1,	0,	0,	0,	0,	0,	1,},
            {0,	1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,},
            {0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	1,},
            {0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	-1,},
            {0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,},
            {0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,},
            {0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,},
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1,},
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,},
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	1,	0,},

    };

    private BitSet Sensibilizadas;
    private BitSet Esperando;
    private long[] tiemposSensibilizados;
    private long[] tiempos = {0,0,0,ALFA,0,0,BETA,0,0,GAMMA,0,0};

    public RDP(){
        Sensibilizadas=new BitSet(COLUMNAS);
        Esperando=new BitSet(COLUMNAS);
        Sensibilizadas.set(3);
        Esperando.set(3);
    }

    public BitSet getSensibilizadas() {
        return Sensibilizadas;
    }

    public boolean dispararRed(int transicion){
        if(puedoDisparar(transicion)){
            actualizarMarcado(transicion);
            actualizarSensibilizadas();
            actualizarTiempos();
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
        if(marcadoActual[6][0]>0||marcadoActual[7][0]>0||marcadoActual[9][0]>0||marcadoActual[10][0]>0)
        {
            Sensibilizadas.clear(7);
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

    public long tiempoRestante(int transicion){
        long tiempoRestante=tiempos[transicion]-(System.currentTimeMillis()-tiemposSensibilizados[transicion]);
        return tiempoRestante;
    }

    private boolean inVentana(int transicion){
        if(tiempoRestante(transicion)<=0) return true;
        return false;
    }

    private void actualizarTiempos(){
        for(int j=0;j<COLUMNAS;j++){
            if (Sensibilizadas.get(j)==true&&Esperando.get(j)==false){
                tiemposSensibilizados[j]=System.currentTimeMillis();
            }
            if(Sensibilizadas.get(j))   {Esperando.set(j);}
            else                        {Esperando.clear(j);}
        }

    }


}