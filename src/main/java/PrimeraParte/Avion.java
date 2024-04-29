package PrimeraParte;

import java.util.Random;
import static java.lang.Thread.interrupted;

public class Avion extends Thread {
    private String id;
    private int capacidadPasajeros;
    private int numeroDeVuelos;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private final int MAX_VUELOS_ANTES_DE_INSPECCION = 15;
    private int numVuelos = 0;
    private int numAleatorio;
    private int numeroPista;
    private PuertaEmbarque puerta;
    private Aerovia aeroviaO;
    private Aerovia aeroviaD;
    private Aeropuerto aeropuertoOrigen;
    private Aeropuerto aeropuertoDestino;

    
    public Avion(String id, Aeropuerto origen, Aeropuerto destino, Aerovia aeroviaO, Aerovia aeroviaD) {
        this.id = id;
        this.capacidadPasajeros = 100 + (int) (Math.random()*200); // Capacidad entre 100 y 300
        this.aeropuertoOrigen = origen;
        this.aeropuertoDestino = destino;
        this.aeroviaO = aeroviaO;
        this.aeroviaD = aeroviaD;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
            
            // VUELO DE IDA    
                
                // DESPEGAR
                
                aeropuertoOrigen.aparecerHangar(this);
                aeropuertoOrigen.salirHangar(this);
                aeropuertoOrigen.entrarEstacionamiento(this);
                Thread.sleep(1000 + (int) (Math.random()*2001)); 
                aeropuertoOrigen.salirEstacionamiento(this);
                aeropuertoOrigen.obtenerPuertaEmbarque(this, aeropuertoOrigen);
                aeropuertoOrigen.liberarPuerta(this.puerta, this);
                aeropuertoOrigen.entrarRodaje(this);
                Thread.sleep(1000 + (int) (Math.random()*4001)); 
                aeropuertoOrigen.solicitarPista(this);
                Thread.sleep(1000 + (int)(Math.random()*2001));
                aeropuertoOrigen.despegar(this, aeropuertoOrigen, numeroPista);
                Thread.sleep(1000 + (int)(Math.random()*4001));
                aeroviaO.accederAerovia(this.id);
                aeropuertoOrigen.volar(this, aeropuertoDestino);
                Thread.sleep(15000 + (int)(Math.random()*15001));
                
                
                // ATERRIZAR
                
                
                while ((numeroPista = aeropuertoDestino.solicitarPista(this)) == -1) {
                    System.out.println("No hay pistas disponibles para el avión con ID " + this.getAvionId() + ". Esperando...");
                    try {
                        aeropuertoDestino.darRodeo(this);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Avión con ID " + this.getAvionId() + " interrumpido mientras esperaba una pista.");
                        Thread.currentThread().interrupt(); // Asegurar una correcta gestión de la interrupción
                        return; // Terminar ejecución del método run si el hilo es interrumpido
                    }
                }
                aeropuertoDestino.aterrizar(this, aeropuertoOrigen, numeroPista);
                aeroviaO.liberarAerovia(id);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoDestino.entrarRodaje(this);
                Thread.sleep(3000 + (int) (Math.random()*2001));
                aeropuertoDestino.obtenerPuertaDesembarque(this, aeropuertoOrigen);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoDestino.salirRodaje(this);
                aeropuertoDestino.liberarPuerta(this.puerta, this);
                aeropuertoDestino.entrarEstacionamiento(this);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoDestino.salirEstacionamiento(this);
                if (numVuelos == 15) {
                    aeropuertoDestino.revisarNecesidadDeInspeccion(this);
                } else {
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                numAleatorio = (int) (Math.random() * 2);
                if (numAleatorio == 0) {
                    aeropuertoDestino.aparecerHangar(this);
                    Thread.sleep((int)(Math.random()* 15001) + 15001);
                } 
                aeropuertoDestino.entrarEstacionamiento(this);
                numVuelos ++;
                
                
            // VUELO DE VUELTA
            
                // DESPEGAR
                
                aeropuertoDestino.aparecerHangar(this);
                aeropuertoDestino.salirHangar(this);
                aeropuertoDestino.entrarEstacionamiento(this);
                Thread.sleep(1000 + (int) (Math.random()*2001)); 
                aeropuertoDestino.salirEstacionamiento(this);
                aeropuertoDestino.obtenerPuertaEmbarque(this, aeropuertoOrigen);
                aeropuertoDestino.liberarPuerta(this.puerta, this);
                aeropuertoDestino.entrarRodaje(this);
                Thread.sleep(1000 + (int) (Math.random()*4001)); 
                aeropuertoDestino.solicitarPista(this);
                Thread.sleep(1000 + (int)(Math.random()*2001));
                aeropuertoDestino.despegar(this, aeropuertoOrigen, numeroPista);
                Thread.sleep(1000 + (int)(Math.random()*4001));
                aeroviaD.accederAerovia(this.id);
                aeropuertoDestino.volar(this, aeropuertoOrigen);
                Thread.sleep(15000 + (int)(Math.random()*15001));
                
                
                // ATERRIZAR
                
                
                while ((numeroPista = aeropuertoOrigen.solicitarPista(this)) == -1) {
                    System.out.println("No hay pistas disponibles para el avión con ID " + this.getAvionId() + ". Esperando...");
                    try {
                        aeropuertoOrigen.darRodeo(this);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Avión con ID " + this.getAvionId() + " interrumpido mientras esperaba una pista.");
                        Thread.currentThread().interrupt(); // Asegurar una correcta gestión de la interrupción
                        return; // Terminar ejecución del método run si el hilo es interrumpido
                    }
                }
                aeropuertoOrigen.aterrizar(this, aeropuertoOrigen, numeroPista);
                aeroviaD.liberarAerovia(id);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoOrigen.entrarRodaje(this);
                Thread.sleep(3000 + (int) (Math.random()*2001));
                aeropuertoOrigen.obtenerPuertaDesembarque(this, aeropuertoOrigen);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoOrigen.salirRodaje(this);
                aeropuertoOrigen.liberarPuerta(this.puerta, this);
                aeropuertoOrigen.entrarEstacionamiento(this);
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoOrigen.salirEstacionamiento(this);
                if (numVuelos == 15) {
                    aeropuertoOrigen.revisarNecesidadDeInspeccion(this);
                } else {
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                numAleatorio = (int) (Math.random() * 2);
                if (numAleatorio == 0) {
                    aeropuertoOrigen.aparecerHangar(this);
                    Thread.sleep((int)(Math.random()* 15001) + 15001);
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
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------ GETTERS Y SETTERS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
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

    public PuertaEmbarque getPuerta() {
        return puerta;
    }
    
    public void setPuerta(PuertaEmbarque puerta) {
        this.puerta = puerta;
    }

    public Aeropuerto getAeropuertoOrigen() {
        return aeropuertoOrigen;
    }

    public Aeropuerto getAeropuertoDestino() {
        return aeropuertoDestino;
    }
    
    
    
}