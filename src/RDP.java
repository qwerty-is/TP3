
import java.util.BitSet;

public class RDP {

    private final int M = 15;
    private final int N = 16;
    private final int FILAS=16;     //n -> Numero de plazas
    private final int COLUMNAS=15;  //m -> Numero de transiciones

    private final int FILAS_H = M;
    private final int COLUMNAS_H = N;

    private final long ALFA=5;
    private final long BETA=10;
    private final long GAMMA=10;

    //Esto debería ser nx1, pero por alguna razon la hice 1xn
    private int[][] marcadoActual={{1},{0},{0},{0},{1},{0},{0},{0},{1},{0},{0},{1},{1},{0},{0},{0}};

    //Esto debería ser nxm pero por alguna razón extraña la trabajé como mxn
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
    //La cuenta está hecha como si fuera mxn
    private boolean[][] matrizH={
            {},
            {},
            {},
            {},
            {},
    };

    private BitSet Sensibilizadas;
    private BitSet Esperando;
    private BitSet Insensibilizadas;
    private long[] tiemposSensibilizados = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private long[] tiempos = {0,0,0,ALFA,0,0,BETA,0,0,GAMMA,0,0,0,0,0};

    public RDP(){
        Sensibilizadas=new BitSet(COLUMNAS);
        Esperando=new BitSet(COLUMNAS);
        Insensibilizadas = new BitSet(COLUMNAS);
        actualizarSensibilizadas();
        actualizarInsensibilizadas();
        actualizarTiempos();
    }

    public BitSet getSensibilizadas() {
        return Sensibilizadas;
    }

    public BitSet getInsensibilizadas(){return Insensibilizadas;}

    public void dispararRed(int transicion){
        actualizarMarcado(transicion);
        actualizarSensibilizadas();
        actualizarInsensibilizadas();
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

    }

    private void actualizarInsensibilizadas(){

        Insensibilizadas.set(0,M);
        for(int i=0;i<FILAS_H;i++){
            for(int j=0;j<COLUMNAS_H;j++){
                if(matrizH[i][j] && marcadoActual[0][j]>0){
                    Insensibilizadas.clear(i);
                    break;
                }
            }
        }

    }

    public boolean puedoDisparar(int transicion){
        return Sensibilizadas.get(transicion) && Insensibilizadas.get(transicion);
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