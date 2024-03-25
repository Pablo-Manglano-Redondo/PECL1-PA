package PrimeraParte;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compartida {

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
}
