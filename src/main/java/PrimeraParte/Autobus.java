package PrimeraParte;

import java.util.ArrayList;

public class Autobus extends Thread {
    private final String id;
    private ArrayList <Autobus> autobuses = new ArrayList<>();
    private final Aeropuerto a;
    private Detener d;
    public Autobus(String id, Aeropuerto a, Detener d) {
        this.id = id;
        this.a = a;
        this.d = d;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // Llegada a la parada del centro de la ciudad y espera de pasajeros   
                d.esperar();
                a.llegadaParadaCentro(id, a);
                Thread.sleep(2000 + (int)(Math.random()*3001));
                // Se generan los pasajeros
                int pasajeros = generarPasajeros();
                // Viaje hacia el aeropuerto
                d.esperar();
                a.marchaAeropuerto(pasajeros, id, a);
                Thread.sleep(5000 + (int)(Math.random()*5001));
                // Llegada de los pasajeros al aeropuerto
                d.esperar();
                a.llegadaAeropuerto(pasajeros, a);
                // Espera nuevos pasajeros
                d.esperar();
                a.esperarNuevosPasajeros(id, a);
                // Salida del aeropuerto
                d.esperar();
                a.salidaAeropuerto(generarPasajeros(), a);
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
