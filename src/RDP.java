
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;

public class RDP {

    private final int N_FILAS=16;     //n → Número de plazas
    private final int N_COLUMNAS=15;  //m → Número de transiciones

    private final long ALFA=5;      //Tiempo en ms que tarda en generarse una tarea
    private final long BETA=10;     //Tiempo en ms que demora el core 1 en realizar una tarea
    private final long GAMMA=10;    //Tiempo en ms que demora el core 2 en realizar una tarea

    //Marcado actual. Empieza con los valores del estado inicial de la red
    private int[][] marcadoActual={{1},{0},{0},{0},{1},{0},{0},{0},{1},{0},{0},{1},{1},{0},{0},{0}};

    //Matriz diferencia entre I+ e I-
    private final int[][] matrizW={

            {-1, 0,	0, 0, 0, 0,	0, 1, 0, 0,	0, 0, 0, 0,	0}, //P0
            { 1,-1,	0, 0, 0, 0,	0, 0, 0, 0,	0, 0, 0, 0,	0}, //P1
            { 0,-1,-1, 0, 1, 0,	0, 0, 0, 0,	0, 0, 0, 0,	0}, //P2
            { 0, 1,	0, 0, 0, 0,	0,-1, 0, 0,	0, 0, 0, 0,	0}, //P3
            { 0, 0,	0,-1, 1, 0,	0, 0, 0, 0,	1, 0, 0, 0,	0}, //P4
            { 0, 0,	0, 1,-1, 0,	0, 0, 0, 0,-1, 0, 0, 0,	0}, //P5
            { 0, 0,	0, 0, 1,-1,	0, 0, 0, 0,	0, 0, 0, 0,	0}, //P6
            { 0, 0,	0, 0, 0, 1,-1, 0, 0, 0,	0, 0, 0, 0,	0}, //P7
            { 0, 0,	0, 0, 0,-1,	1, 0, 0, 0,	0, 0, 0, 0,	0}, //P8
            { 0, 0,	0, 0, 0, 0,	0, 0,-1, 0,	1, 0, 0, 0,	0}, //P9
            { 0, 0,	0, 0, 0, 0,	0, 0, 1,-1,	0, 0, 0, 0,	0}, //P10
            { 0, 0,	0, 0, 0, 0,	0, 0,-1, 1,	0, 0, 0, 0,	0}, //P11
            { 0, 0,	0, 0, 0, 0,	0, 0, 0, 0,	0, 0,-1, 0,	1}, //P12
            { 0, 0,	0, 0, 0, 0,	0, 0, 0, 0,	1,-1, 0,-1,	0}, //P13
            { 0, 0,	0, 0, 0, 0,	0, 0, 0, 0,	0, 0, 1,-1,	0}, //P14
            { 0, 0,	0, 0, 0, 0,	0, 0, 0, 0,	0, 0, 0, 1,-1}, //P15

    };
    //Matriz I-
    private final int[][] matrizI={
            {1, 0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P0
            {0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P1
            {1, 1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P2
            {0,	0,	1,	0,	0,	1,	0,	1,	0,	0,	0,	0,	0,	0,	0}, //P3
            {0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P4
            {0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0}, //P5
            {0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P6
            {0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0}, //P7
            {0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0}, //P8
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0}, //P9
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0}, //P10
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0}, //P11
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0}, //P12
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	1,	0}, //P13
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0}, //P14
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1}, //P15
    };

    //Matriz de inhibiciones. 1 si la transición 'j' tiene arco inhibidor de la plaza 'i'
    private final int[][] matrizH={
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P0
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P1
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P2
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P3
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P4
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P5
            {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},    // P6
            {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},    // P7
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P8
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},    // P9
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},    // P10
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P11
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P12
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P13
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P14
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    // P15

    };

    //Vector de transiciones sensibilizadas. Si hay 1 está sensibilizada
    private final BitSet Sensibilizadas;

    //Vector de transiciones insensibilizadas. Si hay 0 está insensibilizada
    private final BitSet Insensibilizadas;

    //Vector de transiciones que esperan a que se cumpla su plazo
    private final BitSet Esperando;

    //Vector que almacena el tiempo que lleva una transición desde que fue sensibilizada
    private final long[] tiemposSensibilizados = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    //Vector que almacena el tiempo que debe pasar una transición desde que es sensibilizada a poder dispararse
    private final long[] tiempos = {0,0,0,ALFA,0,0,BETA,0,0,GAMMA,0,0,0,0,0};

    //Variables necesarias para loguear
    private static final String PATH              = System.getProperty("user.dir")+ "/data/transiciones.txt";

    //Dimensiones de las matrices
    private final int n_filas;
    private final int n_columnas;

    /*
    * Constructor
    * */
    public RDP() {
        //Crea el directorio y el archivo donde se loguearan las transiciones,
        //si no existen
        new File(System.getProperty("user.dir")+ "/data").mkdir();
        File file = new File(PATH);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Se cargan las matrices

        //Esto cargarlo considerando las matrices obtenidas del html
        n_filas = N_FILAS;
        n_columnas = N_COLUMNAS;

        //Crea los vectores de estado y los actualiza considerando el marcado inicial
        Sensibilizadas=new BitSet(n_columnas);
        Esperando=new BitSet(n_columnas);
        Insensibilizadas = new BitSet(n_columnas);
        actualizarSensibilizadas();
        actualizarInsensibilizadas();
        actualizarTiempos();
    }

    /*
    * Retorna el bitset Sensibilizadas
    * */
    public BitSet getSensibilizadas() {
        return Sensibilizadas;
    }

    /*
    * Al dispararse la red, se actualizan los vectores y loguea la transición
    * */
    public void dispararRed(int transicion){
        actualizarMarcado(transicion);
        actualizarSensibilizadas();
        actualizarInsensibilizadas();
        actualizarTiempos();
        loguearTransicionTxt(transicion);
    }

    /*
    * Actualiza el vector de Sensibilizadas. Coloca 1 si la posición 'j' si la transición 'j'
    * se sensibiliza. 0 en caso contrario.
    *
    * */
    private void actualizarSensibilizadas(){
        Sensibilizadas.clear();
        for(int j=0;j<n_columnas;j++){
            for(int i=0;i<n_filas;i++){
                if(marcadoActual[i][0]-matrizI[i][j]<0){
                    break;
                }
                if(i==n_filas-1){
                    Sensibilizadas.set(j);
                }
            }
        }
    }

    /*
     * Actualiza el vector de Insensibilizadas. Coloca 0 si la posición 'j' si la transición 'j'
     * se insensibiliza por arco inhibidor. 1 en caso contrario.
     * */
    private void actualizarInsensibilizadas(){
        Insensibilizadas.set(0,n_columnas);
        for(int j=0;j<n_columnas;j++){
            for(int i=0;i<n_filas;i++){
                if(matrizH[i][j]>0 && marcadoActual[j][0]>0){
                    Insensibilizadas.clear(j);
                    break;
                }
            }
        }
    }

    /*
    * Retorna true si 'transición' está sensibilizada y no inhibida
    * */
    public boolean puedoDisparar(int transicion){
        return Sensibilizadas.get(transicion) && Insensibilizadas.get(transicion);
    }

    /*
    * Actualiza en marcado actual, realizando la suma entre el vector actual, y la columna
    * de la matriz 'W' correspondiente a la transición 'T'
    * */
    private void actualizarMarcado(int T){
        marcadoActual=suma(marcadoActual,obtenerColumna(matrizW,T));
    }

    /*
    * Retorna un vector columna con los valores de la columna 'columna' de la matriz 'matriz'
    * */
    private int[][] obtenerColumna(int[][] matriz,int columna){
        int[][] column=new int[n_filas][1];
        for(int i=0;i<n_filas;i++){
            column[i][0]=matriz[i][columna];
        }
        return column;
    }

    /*
    * Realiza la suma entre 2 vectores y retorna el resultado
    * */
    private int[][] suma(int[][] vec1, int[][] vec2){
        int[][] resultado = new int[n_filas][1];
        for(int i=0;i<n_filas;i++){
            resultado[i][0]=vec1[i][0]+vec2[i][0];
        }
        return resultado;
    }

    /*
    * Retorna el tiempo que resta para que una transición se encuentre en la ventana.
    * Para eso, retorna la resta entre el momento actual y el momento en que se sensibilizó
    * la transición
    * */
    public long tiempoRestante(int transicion){
        return tiempos[transicion]-(System.currentTimeMillis()-tiemposSensibilizados[transicion]);
    }

    /*
    * Retorna true si la transición se encuentra en su ventana de tiempo.
    * */
    public boolean inVentana(int transicion){
        return tiempoRestante(transicion)<=0;
    }

    /*
    * Coloca el tiempo del momento en el que una transición se sensibiliza, si es
    * que esta no estaba sensibilizada antes.
    * */
    private void actualizarTiempos(){
        for(int j=0;j<n_columnas;j++){
            if (Sensibilizadas.get(j)&&!Esperando.get(j)){
                tiemposSensibilizados[j]=System.currentTimeMillis();
            }
            if(Sensibilizadas.get(j))   {Esperando.set(j);}
            else                        {Esperando.clear(j);}
        }
    }

    /*
    * Escribe en un txt la transición que se disparó. Se separa con '-' para facilitar
    * el análisis de invariantes que se realiza con el código en python.
    * */
    private void loguearTransicionTxt(int transicion) {
        Path filePath = Paths.get(PATH);
        String a_escribir = "T"+transicion+"-";
        try {
            Files.writeString(filePath, a_escribir, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}