package PrimeraParte;

import java.util.Random;
import static java.lang.Thread.interrupted;
import java.time.LocalTime;

public class Avion extends Thread {
    private String id;
    private int capacidadPasajeros;
    private int numeroDeVuelos;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private final int MAX_VUELOS_ANTES_DE_INSPECCION = 15;
    private int numVuelos = 0;
    private int numAleatorio;
    private int numeroPista = -1;
    private PuertaEmbarque puerta;
    private Aerovia aeroviaO;
    private Aerovia aeroviaD;
    private Aeropuerto aeropuertoOrigen;
    private Aeropuerto aeropuertoDestino;
    private Detener d;
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private LocalTime horaActual;
    
    public Avion(String id, Aeropuerto origen, Aeropuerto destino, Aerovia aeroviaO, Aerovia aeroviaD, Detener d) {
        this.id = id;
        this.capacidadPasajeros = 100 + (int) (Math.random()*200); // Capacidad entre 100 y 300
        this.aeropuertoOrigen = origen;
        this.aeropuertoDestino = destino;
        this.aeroviaO = aeroviaO;
        this.aeroviaD = aeroviaD;
        this.d = d;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
            
            // VUELO DE IDA    
                
                // DESPEGAR
                horaActual = LocalTime.now();
                int h = horaActual.getHour();
                int m = horaActual.getMinute();
                int s = horaActual.getSecond();
                d.esperar();
                aeropuertoOrigen.aparecerHangar(this);
                d.esperar();
                aeropuertoOrigen.salirHangar(this);
                d.esperar();
                aeropuertoOrigen.entrarEstacionamiento(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*2001)); 
                aeropuertoOrigen.salirEstacionamiento(this);
                d.esperar();
                aeropuertoOrigen.obtenerPuertaEmbarque(this, aeropuertoOrigen);
                d.esperar();
                aeropuertoOrigen.liberarPuerta(this.puerta, this);
                d.esperar();
                aeropuertoOrigen.entrarRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001)); 
                aeropuertoOrigen.solicitarPista(this);
                aeropuertoOrigen.salirRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int)(Math.random()*2001));
                aeropuertoOrigen.despegar(this, aeropuertoOrigen, numeroPista);
                d.esperar();
                Thread.sleep(1000 + (int)(Math.random()*4001));
                aeroviaO.accederAerovia(this.id);
                d.esperar();
                aeropuertoOrigen.volar(this, aeropuertoDestino);
                d.esperar();
                Thread.sleep(15000 + (int)(Math.random()*15001));
                
                
                // ATERRIZAR
                
                
                while ((numeroPista = aeropuertoDestino.solicitarPista(this)) == -1) {
                    ea.escribirLog(h + ":" + m + ":" + s + "--No hay pistas disponibles para el avión con ID " + this.getAvionId() + ". Esperando...");
                    try {
                        aeropuertoDestino.darRodeo(this);
                        d.esperar();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Avión con ID " + this.getAvionId() + " interrumpido mientras esperaba una pista.");
                        Thread.currentThread().interrupt(); // Asegurar una correcta gestión de la interrupción
                        return; // Terminar ejecución del método run si el hilo es interrumpido
                    }
                }
                aeropuertoDestino.aterrizar(this, aeropuertoOrigen, numeroPista);
                d.esperar();
                aeroviaO.liberarAerovia(id);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoDestino.entrarRodaje(this);
                d.esperar();
                Thread.sleep(3000 + (int) (Math.random()*2001));
                aeropuertoDestino.obtenerPuertaDesembarque(this, aeropuertoOrigen);
                aeropuertoDestino.salirRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                d.esperar();
                aeropuertoDestino.liberarPuerta(this.puerta, this);
                d.esperar();
                aeropuertoDestino.entrarEstacionamiento(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoDestino.salirEstacionamiento(this);
                d.esperar();
                if (numVuelos == 15) {
                    aeropuertoDestino.revisarNecesidadDeInspeccion(this);
                d.esperar();
                } else {
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                numAleatorio = (int) (Math.random() * 2);
                if (numAleatorio == 0) {
                    aeropuertoDestino.aparecerHangar(this);
                    d.esperar();
                    Thread.sleep((int)(Math.random()* 15001) + 15001);
                } 
                aeropuertoDestino.entrarEstacionamiento(this);
                d.esperar();
                numVuelos ++;
                
                
            // VUELO DE VUELTA
            
                // DESPEGAR
                
                aeropuertoDestino.aparecerHangar(this);
                d.esperar();
                aeropuertoDestino.salirHangar(this);
                d.esperar();
                aeropuertoDestino.entrarEstacionamiento(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*2001)); 
                aeropuertoDestino.salirEstacionamiento(this);
                d.esperar();
                aeropuertoDestino.obtenerPuertaEmbarque(this, aeropuertoOrigen);
                d.esperar();
                aeropuertoDestino.liberarPuerta(this.puerta, this);
                d.esperar();
                aeropuertoDestino.entrarRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001)); 
                aeropuertoDestino.solicitarPista(this);
                aeropuertoDestino.salirRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int)(Math.random()*2001));
                aeropuertoDestino.despegar(this, aeropuertoOrigen, numeroPista);
                d.esperar();
                Thread.sleep(1000 + (int)(Math.random()*4001));
                aeroviaD.accederAerovia(this.id);
                d.esperar();
                aeropuertoDestino.volar(this, aeropuertoOrigen);
                d.esperar();
                Thread.sleep(15000 + (int)(Math.random()*15001));
                
                
                // ATERRIZAR
                
                
                while ((numeroPista = aeropuertoOrigen.solicitarPista(this)) == -1) {
                    ea.escribirLog(h + ":" + m + ":" + s + "--No hay pistas disponibles para el avión con ID " + this.getAvionId() + ". Esperando...");
                    try {
                        aeropuertoOrigen.darRodeo(this);
                        d.esperar();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Avión con ID " + this.getAvionId() + " interrumpido mientras esperaba una pista.");
                        Thread.currentThread().interrupt(); // Asegurar una correcta gestión de la interrupción
                        return; // Terminar ejecución del método run si el hilo es interrumpido
                    }
                }
                aeropuertoOrigen.aterrizar(this, aeropuertoOrigen, numeroPista);
                d.esperar();
                aeroviaD.liberarAerovia(id);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoOrigen.entrarRodaje(this);
                d.esperar();
                Thread.sleep(3000 + (int) (Math.random()*2001));
                aeropuertoOrigen.obtenerPuertaDesembarque(this, aeropuertoOrigen);
                aeropuertoOrigen.salirRodaje(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                d.esperar();
                aeropuertoOrigen.liberarPuerta(this.puerta, this);
                d.esperar();
                aeropuertoOrigen.entrarEstacionamiento(this);
                d.esperar();
                Thread.sleep(1000 + (int) (Math.random()*4001));
                aeropuertoOrigen.salirEstacionamiento(this);
                d.esperar();
                if (numVuelos == 15) {
                    aeropuertoOrigen.revisarNecesidadDeInspeccion(this);
                    d.esperar();
                } else {
                    Thread.sleep((int)(Math.random()* 4001) + 1000);
                } 
                numAleatorio = (int) (Math.random() * 2);
                if (numAleatorio == 0) {
                    aeropuertoOrigen.aparecerHangar(this);
                    d.esperar();
                    Thread.sleep((int)(Math.random()* 15001) + 15001);
                } 
                aeropuertoOrigen.entrarEstacionamiento(this);
                d.esperar();
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

    public void setNumeroPista(int numeroPista) {
        this.numeroPista = numeroPista;
    }
    
}