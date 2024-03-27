package PrimeraParte;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compartida {

    // MÉTODOS AUTOBUS
    
    public void llegadaParadaCentro() {
        try {
            Thread.sleep(2000 + (int) (Math.random() * 3001));
            Random rand = new Random();
            int numPasajeros = rand.nextInt(51);
            System.out.println("En la parada del centro de la ciudad se han subido " + numPasajeros + " pasajeros");
        } catch (InterruptedException ex) {
            Logger.getLogger(Compartida.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    public void marchaAeropuerto() {
        try {
            Thread.sleep(5000 + (int) (Math.random() * 5001));
            System.out.println("Autobús yendo hacia el aeropuerto");
        } catch (InterruptedException ex) {
            Logger.getLogger(Compartida.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    public void bajarPasajeros() {
        // Simulación de que los pasajeros bajan en el aeropuerto
        System.out.println("Los pasajeros han bajado en el aeropuerto");
    }
    
    // MÉTODOS AVIÓN
    
    public void realizarActividadesDelCicloDeVida() throws InterruptedException {
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

    public void enHangar() throws InterruptedException {
        // Lógica para estar en el hangar
        Thread.sleep(1000); // Ejemplo de espera en el hangar
        System.out.println(Avion.imprimirAvion() + " está en el hangar.");
    }

    public void enAreaDeEstacionamiento() throws InterruptedException {
        // Lógica para estar en el área de estacionamiento
    }

    public void embarcarPasajeros() throws InterruptedException {
        // Lógica para embarcar pasajeros
    }

    public void enAreaDeRodaje() throws InterruptedException {
        // Lógica para estar en el área de rodaje
    }

    public void despegar() throws InterruptedException {
        // Lógica para despegar
        System.out.println(imprimirAvion() + " está despegando.");
    }

    public void enVuelo() throws InterruptedException {
        // Lógica para estar en vuelo
        Thread.sleep(5000); // Ejemplo de tiempo en vuelo
        System.out.println(imprimirAvion() + " está en vuelo.");
    }

    public void aterrizar() throws InterruptedException {
        // Lógica para aterrizar
    }

    public void desembarcarPasajeros() throws InterruptedException {
        // Lógica para desembarcar pasajeros
    }

    public void revisarNecesidadDeInspeccion() throws InterruptedException {
        numeroDeVuelos++;
        if (numeroDeVuelos >= MAX_VUELOS_ANTES_DE_INSPECCION) {
            // Realizar inspección en el taller
            numeroDeVuelos = 0; // Resetear contador después de inspección
            System.out.println(imprimirAvion() + " está en inspección.");
            Thread.sleep(2000); // Simular tiempo de inspección
        }
    }
}
