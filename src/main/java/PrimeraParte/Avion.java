package PrimeraParte;

import java.util.Random;

public class Avion extends Thread {
    public static String id, destino;
    private int capacidadPasajeros;
    public static int numeroDeVuelos;
    private final Compartida recursoCompartido;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    public static final int MAX_VUELOS_ANTES_DE_INSPECCION = 15;
    private int numVuelos = 0;
    private static int numAleatorio;
    
    public Avion(int numero, Compartida recursoCompartido) {
        Random rand = new Random();
        char letra1 = abecedario.charAt(rand.nextInt(abecedario.length()));
        char letra2 = abecedario.charAt(rand.nextInt(abecedario.length()));
        this.id = String.format("%c%c-%04d", letra1, letra2, numero);
        this.destino = (numero % 2 == 0) ? "Madrid" : "Barcelona";
        this.capacidadPasajeros = 100 + rand.nextInt(201); // Capacidad entre 100 y 300
        this.recursoCompartido = recursoCompartido;
        this.numeroDeVuelos = 0;
    }

    Aeropuerto ae = new Aeropuerto();
    
    @Override
    public void run() {
        try {
            while (!interrupted()) {
                // Simula el ciclo de vida del avión aquí
                //recursoCompartido.realizarActividadesDelCicloDeVida(id);
                ae.entrarHangar(this);
                ae.salirHangar(this);
                ae.entrarEstacionamiento(this);
                ae.esperarPuertaEmbarque();
                ae.entrarPuertaEmbarque(this);
                ae.embarcarPasajeros();
                ae.salirPuertaEmbarque(this);
                ae.entrarRodaje(this);
                ae.esperarPista();
                ae.entrarPista(this);
                ae.despegar();
                //Accede a la aerovía
                ae.volar();
                ae.esperarPista();
                while (!ae.solicitarPista()) {
                    ae.darRodeo();
                }
                ae.entrarPista(this);
                ae.salirPista(this);
                ae.entrarRodaje(this);
                ae.esperarPuertaEmbarque();
                ae.salirRodaje(this);
                ae.entrarPuertaEmbarque(this);
                ae.desembarcarPasajeros();
                ae.entrarEstacionamiento(this);
                if (numVuelos == 15) {
                    ae.revisarNecesidadDeInspeccion(id, this);
                } else {
                    //sleep
                } 
                numAleatorio =(int) Math.random() * 2;
                if (numAleatorio == 0) {
                    ae.entrarHangar(this);
                    //Sleep
                } 
                ae.entrarEstacionamiento(this);
                numVuelos ++;
            }
        } catch (InterruptedException e) {
            System.out.println("El avión con ID: " + id + " ha sido interrumpido.");
        }
    }

   /* public String imprimirAvion() {
        return "Avión " + this.id + " con destino a " + this.destino + ", capacidad: " + this.capacidadPasajeros + " pasajeros";
    }*/
}