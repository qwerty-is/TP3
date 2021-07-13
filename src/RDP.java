
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;

public class RDP {

    private final int M = 15;       //Numero de transiciones
    private final int N = 16;       //Numero de plazas
    private final int FILAS=16;     //n -> Numero de plazas
    private final int COLUMNAS=15;  //m -> Numero de transiciones

    private final int FILAS_H = M;
    private final int COLUMNAS_H = N;

    private final long ALFA=5;      //Tiempo en ms que tarda en generarse una tarea
    private final long BETA=10;     //Tiempo en ms que demora el procesador 1 en realizar una tarea
    private final long GAMMA=10;    //Tiempo en ms que demora el procesador 2 en realizar una tarea

    //Esto debería ser nx1, pero por alguna razon la hice 1xn
    private int[][] marcadoActual={{1},{0},{0},{0},{1},{0},{0},{0},{1},{0},{0},{1},{1},{0},{0},{0}};

    //
    private int[][] matrizW={

            {-1,0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0}, //P0
            {1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P1
            {0,	-1,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P2
            {0,	1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,	0,	0,	0,	0}, //P3
            {0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0}, //P4
            {0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,	0}, //P5
            {0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P6
            {0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0}, //P7
            {0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	0,	0,	0}, //P8
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0}, //P9
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0}, //P10
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0}, //P11
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1}, //P12
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,	-1,	0}, //P13
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0}, //P14
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1},//P15

    };
    //3,5
    //15,8
    private int[][] matrizI={
            {-1,0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0}, //P0
            {1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P1
            {-1,-1,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P2
            {0,	1,	-1,	0,	0,	-1,	0,	-1,	0,	0,	0,	0,	0,	0,	0}, //P3
            {0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0}, //P4
            {0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	-1,	0,	0,	0,	0}, //P5
            {0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P6
            {0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0,	0,	0,	0}, //P7
            {0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0,	0,	0,	0}, //P8
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0}, //P9
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0,	0,	0,	0,	0}, //P10
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	1,	0,	0,	0,	0,	0}, //P11
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	1}, //P12
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	-1,	-1,	0}, //P13
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1,	0}, //P14
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	0,	0,	-1,	0,	1,	-1},//P15
    };

    //La cuenta está hecha como si fuera mxn
    //La matriz de petrinet sale nxm
    private int[][] matrizH={
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //0
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //1
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //2
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //3
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //4
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //5
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //6
            {0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0},    //7
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //8
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //9
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //10
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //11
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //12
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    //13
            {0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0},    //14


    };

    private BitSet Sensibilizadas;      //Vector de transiciones sensibilizadas. Si hay 1 está sensibilizada
    private BitSet Esperando;           //Vector de transiciones que esperan a que se cumpla su plazo
    private BitSet Insensibilizadas;    //Vector de transiciones insensibilizadas. Si hay 0 está insensibilizada
    private long[] tiemposSensibilizados = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //Vector que almacena el tiempo que lleva una transicion desde que fue sensibilizada
    private long[] tiempos = {0,0,0,ALFA,0,0,BETA,0,0,GAMMA,0,0,0,0,0};     //Vector que almacena el tiempo que debe pasar una transicion desde que es sensibilizada a poder dispararse

    private static final String PATH                    = System.getProperty("user.dir")+ "/data/transiciones.txt";
    private final BufferedReader br;
    private final File file;

    public RDP() throws IOException {
        new File(System.getProperty("user.dir")+ "/data").mkdir();
        file = new File(PATH);
        file.createNewFile();
        br = new BufferedReader(new FileReader(file));

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
        loguearTransicionTxt(transicion);

    }

    private void actualizarSensibilizadas(){
        Sensibilizadas.clear();
        for(int j=0;j<COLUMNAS;j++){
            for(int i=0;i<FILAS;i++){
                if(marcadoActual[i][0]-matrizI[i][j]<0){
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
                if(matrizH[i][j]>0 && marcadoActual[j][0]>0){
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

    private void loguearTransicionTxt(int n_transicion) {
        Path filePath = Paths.get(PATH);
        String a_escribir = "T"+n_transicion+"-";
        try {
            Files.writeString(filePath, a_escribir, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}