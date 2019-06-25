
import java.util.BitSet;

public class RDP {

    private final int FILAS=16;
    private final int COLUMNAS=15;
    private final long ALFA=5;
    private final long BETA=10;
    private final long GAMMA=10;

    private int[][] marcadoActual={{1},{0},{0},{0},{1},{0},{0},{0},{1},{0},{0},{1},{1},{0},{0},{0}};
    private int[][] matrizW={

            {-1,0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0},
            {1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	-1,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0},
            {0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,	0},
            {0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,	-1,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1},

    };

    private BitSet Sensibilizadas;
    private BitSet Esperando;
    private long[] tiemposSensibilizados = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private long[] tiempos = {0,0,0,ALFA,0,0,BETA,0,0,GAMMA,0,0,0,0,0};

    public RDP(){
        Sensibilizadas=new BitSet(COLUMNAS);
        Esperando=new BitSet(COLUMNAS);
        actualizarSensibilizadas();
        actualizarTiempos();
    }

    public BitSet getSensibilizadas() {
        return Sensibilizadas;
    }

    public void dispararRed(int transicion){
        actualizarMarcado(transicion);
        actualizarSensibilizadas();
        actualizarTiempos();

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
        if(marcadoActual[6][0]>0||marcadoActual[7][0]>0)
        {
            Sensibilizadas.clear(7);
        }
        if(marcadoActual[9][0]>0||marcadoActual[10][0]>0){
            Sensibilizadas.clear(14);
        }
        if(marcadoActual[2][0]==0){
            Sensibilizadas.clear(0);
        }
        if(marcadoActual[13][0]==0){
            Sensibilizadas.clear(12);
        }

        if(marcadoActual[3][0]==0){
            Sensibilizadas.clear(2);
            Sensibilizadas.clear(5);
        }
        if(marcadoActual[15][0]==0){
            Sensibilizadas.clear(8);
            Sensibilizadas.clear(11);
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

    public boolean inVentana(int transicion){
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