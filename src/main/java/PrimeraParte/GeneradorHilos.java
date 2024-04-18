package PrimeraParte;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.CountDownLatch;

public class GeneradorHilos extends Thread{
     
    private Aeropuerto madrid;
    private Aeropuerto barcelona;
    private int numAviones;
    private int numAutobuses;
    private CountDownLatch latch;
    private static final String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    
    public GeneradorHilos(int numAviones, int numAutobuses) {   
        this.numAviones = numAviones;
        this.numAutobuses = numAutobuses;
        this.madrid = new Aeropuerto("Aeropuerto de Madrid");
        this.barcelona = new Aeropuerto("Aeropuerto de Barcelona");
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
        System.out.println("Comenzando a generar aviones, cantidad: " + numAviones);
        Random rand = new Random();
        for (int i = 0; i < numAviones; i++) {
            try {
                String idAvion = Avion.generarIdAvion(i);
                Aeropuerto ae = rand.nextBoolean() ? madrid: barcelona;
                Avion a = new Avion(idAvion, ae, latch);
                ae.agregarAvion(a);
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
                Autobus autobus = new Autobus(idAutobus, ae, latch);
                autobus.start();
                Thread.sleep((int)(Math.random()* 500) + 500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneradorHilos.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();  // Buena práctica para manejar interrupción
            }
        }
    }
}   
