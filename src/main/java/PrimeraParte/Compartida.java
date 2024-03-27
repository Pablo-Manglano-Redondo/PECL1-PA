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
    
    /*public void realizarActividadesDelCicloDeVida(String id) throws InterruptedException {
        // Estas llamadas son ejemplos. Deberías implementar la lógica específica para cada etapa.
        enHangar(id);
        enAreaDeEstacionamiento();
        embarcarPasajeros();
        enAreaDeRodaje();
        despegar(id);
        enVuelo(id);
        aterrizar();
        desembarcarPasajeros();
        revisarNecesidadDeInspeccion(id);
    }*/

    /*public void enHangar(String id) throws InterruptedException {
        // Lógica para estar en el hangar
        Thread.sleep(1000); // Ejemplo de espera en el hangar
        System.out.println("El avión con ID: " + id + " está en el hangar.");
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

    public void despegar(String id) throws InterruptedException {
        // Lógica para despegar
        System.out.println("El avión con ID: " + id + " está despegando.");
    }

    public void enVuelo(String id) throws InterruptedException {
        // Lógica para estar en vuelo
        Thread.sleep(5000); // Ejemplo de tiempo en vuelo
        System.out.println("El avión con ID: " + id + " está en vuelo.");
    }

    public void aterrizar() throws InterruptedException {
        // Lógica para aterrizar
    }

    public void desembarcarPasajeros() throws InterruptedException {
        // Lógica para desembarcar pasajeros
    }
*/
    /*public void revisarNecesidadDeInspeccion(String id) throws InterruptedException {
        Avion.numeroDeVuelos++;
        if (Avion.numeroDeVuelos >= Avion.MAX_VUELOS_ANTES_DE_INSPECCION) {
            // Realizar inspección en el taller
            Avion.numeroDeVuelos = 0; // Resetear contador después de inspección
            System.out.println("El avión con ID: " + id + " está en inspección.");
            Thread.sleep(2000); // Simular tiempo de inspección
        }
    }*/
}
