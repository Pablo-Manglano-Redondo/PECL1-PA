package PrimeraParte;

import java.util.ArrayList;

public class Autobus extends Thread {
    private final String id;
    private ArrayList <Autobus> autobuses = new ArrayList<>();
    private final Aeropuerto aero;
    private Detener d;
    public Autobus(String id, Aeropuerto aero, Detener d) {
        this.id = id;
        this.aero = aero;
        this.d = d;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                aero.actualizarPasajeros();
                // Llegada a la parada del centro de la ciudad y espera de pasajeros   
                d.esperar();
                aero.llegadaParadaCentro(id, aero);
                Thread.sleep(2000 + (int)(Math.random()*3001));
                // Se generan los pasajeros
                int pasajeros = 1 + (int)(Math.random()*49);
                // Viaje hacia el aeropuerto
                d.esperar();
                aero.marchaAeropuerto(pasajeros, id, aero);
                Thread.sleep(5000 + (int)(Math.random()*5001));
                // Llegada de los pasajeros al aeropuerto
                d.esperar();
                aero.llegadaAeropuerto(pasajeros, aero);
                aero.actualizarPasajeros();
                // Espera nuevos pasajeros
                d.esperar();
                aero.esperarNuevosPasajeros(id, aero);
                // Salida del aeropuerto
                d.esperar();
                aero.salidaAeropuerto(1 + (int)(Math.random()*49), aero);
                aero.actualizarPasajeros();
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
     
}
