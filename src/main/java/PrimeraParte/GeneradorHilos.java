package PrimeraParte;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.CountDownLatch;

public class GeneradorHilos extends Thread{
     
    private Aeropuerto madrid;
    private Aeropuerto barcelona;
    private Aerovia aeroviaMadridBarcelona;
    private Aerovia aeroviaBarcelonaMadrid;
    private int numAviones;
    private int numAutobuses;
    private CountDownLatch latch;
    
    public GeneradorHilos(int numAviones, int numAutobuses, Aerovia aeroviaMadridBarcelona, Aerovia aeroviaBarcelonaMadrid, Aeropuerto madrid, Aeropuerto barcelona) {   
        this.numAviones = numAviones;
        this.numAutobuses = numAutobuses;
        this.madrid = madrid;
        this.barcelona = barcelona;
        this.aeroviaMadridBarcelona = aeroviaMadridBarcelona;
        this.aeroviaBarcelonaMadrid = aeroviaBarcelonaMadrid;
        this.latch = new CountDownLatch(numAviones + numAutobuses);
    }
    
    public void run() {
        Thread threadAviones = new Thread(this::generarAviones);
        Thread threadAutobuses = new Thread(this::generarAutobuses);

        threadAviones.start();
        threadAutobuses.start();

        try {
            latch.await(); // Espera a que todos los hilos se completen
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneradorHilos.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    
    private void generarAviones() {
        for (int i = 0; i < numAviones; i++) {
            try {
                String idAvion = Avion.generarIdAvion(i);
                Aeropuerto origen = (i % 2 == 0) ? madrid : barcelona;
                Aerovia aerovia = (origen == madrid) ? aeroviaMadridBarcelona : aeroviaBarcelonaMadrid;
                Avion a = new Avion(idAvion, origen, aerovia);
                origen.agregarAvion(a);
                a.start();
                Thread.sleep((int)(Math.random()*2000) + 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneradorHilos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void generarAutobuses() {
        for (int i = 0; i < numAutobuses; i++) {
            try {
                String idAutobus = "B-" + String.format("%04d", i);  // Genera el ID aquí
                Aeropuerto ae = (i % 2 == 0) ? madrid : barcelona;  // Asigna según par o impar
                Autobus autobus = new Autobus(idAutobus, ae);
                autobus.start();
                Thread.sleep((int)(Math.random()* 500) + 500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneradorHilos.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();  // Buena práctica para manejar interrupción
            }
        }
    }
}   
