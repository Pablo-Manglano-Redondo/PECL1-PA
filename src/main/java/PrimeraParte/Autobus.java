package PrimeraParte;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Autobus extends Thread {
    private final String id;
    private ArrayList <Autobus> autobuses = new ArrayList<>();
    private final Aeropuerto a;
    
    public Autobus(String id, Aeropuerto a) {
        this.id = id;
        this.a = a;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // Llegada a la parada del centro de la ciudad y espera de pasajeros                
                a.llegadaParadaCentro(id, a);
                // Se generan los pasajeros
                int pasajeros = generarPasajeros();
                // Viaje hacia el aeropuerto
                a.marchaAeropuerto(pasajeros, id, a);
                // Llegada de los pasajeros al aeropuerto
                a.llegadaAeropuerto(pasajeros, a);
                // Espera nuevos pasajeros
                a.esperarNuevosPasajeros(id, a);
                // Generar nuevos pasajeros
                int nuevosPasajeros = generarPasajeros();
                // Salida del aeropuerto
                a.salidaAeropuerto(nuevosPasajeros, a);
            }
        } catch (Exception e) {
            System.out.println(this.id + " ha sido interrumpido.");
        }
    }
    public synchronized void agregarAutobus(Autobus autobus) {
        autobuses.add(this);
    }
    
     public String generarIdAutobus(int n) {
        return "B-" + String.format("%04d", n);
    }
     
     public int generarPasajeros() {
         System.out.println("Arreglar esto para que se sincronice con los pasajeros que han aterrizado");
         return (1);
     }
     
}
