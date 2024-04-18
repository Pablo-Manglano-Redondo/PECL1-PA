package PrimeraParte;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import static java.lang.Thread.interrupted;

public class Avion extends Thread {
    private String id;
    private int capacidadPasajeros;
    private int numeroDeVuelos;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private final int MAX_VUELOS_ANTES_DE_INSPECCION = 15;
    private int numVuelos = 0;
    private int numAleatorio;
    private CountDownLatch latch;
    private Aeropuerto aeropuertoOrigen;

    
    public Avion(String id, Aeropuerto aeropuerto, CountDownLatch latch) {
        this.id = id;
        this.capacidadPasajeros = 100 + (int) (Math.random()*200); // Capacidad entre 100 y 300
        this.aeropuertoOrigen = aeropuerto;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                // Simula el ciclo de vida del avión aquí
                aeropuertoOrigen.entrarHangar(this);
                Thread.sleep(15000 + (int) (Math.random()*15001)); // Reposo inicial entre 15 y 30 segundos
                aeropuertoOrigen.salirHangar(this);
                aeropuertoOrigen.entrarEstacionamiento(this);
                aeropuertoOrigen.esperarPuertaEmbarque(this);
                aeropuertoOrigen.entrarPuertaEmbarque(this);
                aeropuertoOrigen.embarcarPasajeros(this, capacidadPasajeros);
                aeropuertoOrigen.salirPuertaEmbarque(this);
                aeropuertoOrigen.entrarRodaje(this);
                Thread.sleep(1000 + (int) (Math.random()*4001)); // Reposo inicial entre 15 y 30 segundos
                aeropuertoOrigen.esperarPista(this);
                aeropuertoOrigen.entrarPista(this);
                aeropuertoOrigen.despegar(this);
                aeropuertoOrigen.accederAerovia(this, aeropuertoOrigen);
                aeropuertoOrigen.volar(this, aeropuertoOrigen);
                aeropuertoOrigen.esperarPista(this);
                while (!aeropuertoOrigen.solicitarPista()) {
                    aeropuertoOrigen.darRodeo(this);
                }
                aeropuertoOrigen.entrarPista(this);
                aeropuertoOrigen.salirPista(this);
                aeropuertoOrigen.entrarRodaje(this);
                aeropuertoOrigen.esperarPuertaEmbarque(this);
                aeropuertoOrigen.salirRodaje(this);
                aeropuertoOrigen.entrarPuertaEmbarque(this);
                aeropuertoOrigen.desembarcarPasajeros(this, capacidadPasajeros);
                aeropuertoOrigen.entrarEstacionamiento(this);
                if (numVuelos == 15) {
                    aeropuertoOrigen.revisarNecesidadDeInspeccion(this);
                } else {
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                numAleatorio =(int) Math.random() * 2;
                if (numAleatorio == 0) {
                    aeropuertoOrigen.entrarHangar(this);
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                aeropuertoOrigen.entrarEstacionamiento(this);
                numVuelos ++;
            }
        } catch (InterruptedException e) {
            System.out.println("El avión con ID: " + id + " ha sido interrumpido.");
        }
    }

    public static String generarIdAvion(int numero) {
        Random rand = new Random();
        char letra1 = abecedario.charAt(rand.nextInt(abecedario.length()));
        char letra2 = abecedario.charAt(rand.nextInt(abecedario.length()));
        String id = String.format("%c%c-%04d", letra1, letra2, numero);
        return id;
    }
    
    public String getAvionId() {
        return id;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public int getNumeroDeVuelos() {
        return numeroDeVuelos;
    }

    public void setNumeroDeVuelos(int numeroDeVuelos) {
        this.numeroDeVuelos = numeroDeVuelos;
    }

    public int getMAX_VUELOS_ANTES_DE_INSPECCION() {
        return MAX_VUELOS_ANTES_DE_INSPECCION;
    }
}