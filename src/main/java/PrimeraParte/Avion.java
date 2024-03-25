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
                realizarActividadesDelCicloDeVida();
            }
        } catch (InterruptedException e) {
            System.out.println(getDescripcion() + " ha sido interrumpido.");
        }
    }

    private void realizarActividadesDelCicloDeVida() throws InterruptedException {
        // Estas llamadas son ejemplos. Deberías implementar la lógica específica para cada etapa.
        enHangar();
        enAreaDeEstacionamiento();
        embarcarPasajeros();
        enAreaDeRodaje();
        despegar();
        enVuelo();
        aterrizar();
        desembarcarPasajeros();
        revisarNecesidadDeInspeccion();
    }

    private void enHangar() throws InterruptedException {
        // Lógica para estar en el hangar
        Thread.sleep(1000); // Ejemplo de espera en el hangar
        System.out.println(getDescripcion() + " está en el hangar.");
    }

    private void enAreaDeEstacionamiento() throws InterruptedException {
        // Lógica para estar en el área de estacionamiento
    }

    private void embarcarPasajeros() throws InterruptedException {
        // Lógica para embarcar pasajeros
    }

    private void enAreaDeRodaje() throws InterruptedException {
        // Lógica para estar en el área de rodaje
    }

    private void despegar() throws InterruptedException {
        // Lógica para despegar
        System.out.println(getDescripcion() + " está despegando.");
    }

    private void enVuelo() throws InterruptedException {
        // Lógica para estar en vuelo
        Thread.sleep(5000); // Ejemplo de tiempo en vuelo
        System.out.println(getDescripcion() + " está en vuelo.");
    }

    private void aterrizar() throws InterruptedException {
        // Lógica para aterrizar
    }

    private void desembarcarPasajeros() throws InterruptedException {
        // Lógica para desembarcar pasajeros
    }

    private void revisarNecesidadDeInspeccion() throws InterruptedException {
        numeroDeVuelos++;
        if (numeroDeVuelos >= MAX_VUELOS_ANTES_DE_INSPECCION) {
            // Realizar inspección en el taller
            numeroDeVuelos = 0; // Resetear contador después de inspección
            System.out.println(getDescripcion() + " está en inspección.");
            Thread.sleep(2000); // Simular tiempo de inspección
        }
    }

    public String getDescripcion() {
        return "Avión " + this.id + " con destino a " + this.destino + ", capacidad: " + this.capacidadPasajeros + " pasajeros";
    }
}
