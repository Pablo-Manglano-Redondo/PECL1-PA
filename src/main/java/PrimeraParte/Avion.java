package PrimeraParte;

import java.util.Random;

public class Avion extends Thread {
    private String id, destino;
    private int capacidadPasajeros;
    private int numeroDeVuelos;
    private final Compartida recursoCompartido;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private static final int MAX_VUELOS_ANTES_DE_INSPECCION = 15;

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

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                // Simula el ciclo de vida del avión aquí
                recursoCompartido.realizarActividadesDelCicloDeVida();
            }
        } catch (InterruptedException e) {
            System.out.println(imprimirAvion() + " ha sido interrumpido.");
        }
    }

    public String imprimirAvion() {
        return "Avión " + this.id + " con destino a " + this.destino + ", capacidad: " + this.capacidadPasajeros + " pasajeros";
    }
}
