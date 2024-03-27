package PrimeraParte;

import java.util.concurrent.Semaphore;


public class Aeropuerto {
    
    private Semaphore esperarPuertaEmbarque = new Semaphore(6, true);
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    public synchronized void actualizarPasajeros(/* parámetros */) {
        // Lógica para actualizar el número de pasajeros
    }
    
    public synchronized void entrarHangar(Avion a){
        System.out.println("El avión con ID: " + a.id + " ha entrado en el hangar");
    }
    
    public synchronized void salirHangar(Avion a){
        System.out.println("El avión con ID: " + a.id + " ha salido del hangar");
    }
    
    public synchronized void entrarTaller(Avion a) throws InterruptedException {
        capacidadTaller.acquire();
        System.out.println("El avión con ID: " + a.id + " ha entrado en el hangar");
    }
    
    public synchronized void salirTaller(Avion a) {
        capacidadTaller.release();
        System.out.println("El avión con ID: " + a.id + " ha salido en el hangar");
    }
    
    public void entrarPuertaEmbarque(Avion a) throws InterruptedException {
        esperarPuertaEmbarque.acquire();
        System.out.println("El avión con ID: " + a.id + " ha entrado por la puerta de embarque");
        
    }
    public void esperarPuertaEmbarque() {
        
    }
    
    public void embarcarPasajeros() {
        
    }
    
    public void desembarcarPasajeros() {
        
    }
    
    public void esperarPista() {
        
    }
    
    public void despegar() {
        
    }
    
    public void volar() {
        
    }
    
    public boolean solicitarPista() {
        
        return false;
        
    }
    
    public void darRodeo() {
        
    }
    public void salirPuertaEmbarque(Avion a) {
        esperarPuertaEmbarque.release();
    }
    
    public synchronized void entrarPista(Avion a) throws InterruptedException {
        capacidadPistas.acquire();
    }
    
    public synchronized void salirPista(Avion a) {
        capacidadPistas.release();
    }
    
    public synchronized void entrarEstacionamiento(Avion a) {
        System.out.println("El avión con ID: " + a.id + " ha entrado en el área de estacionamiento");
    }
    
    public synchronized void salirEstacionamiento(Avion a) {
        System.out.println("El avión con ID: " + a.id + " ha salido en el área de estacionamiento");
    }
    
    public synchronized void entrarRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.id + " ha entrado en el área de rodaje");
    }
    
    public synchronized void salirRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.id + " ha salido del área de rodaje");
    }
    
    public void revisarNecesidadDeInspeccion(String id, Avion a) throws InterruptedException {
        a.numeroDeVuelos++;
        if (Avion.numeroDeVuelos >= Avion.MAX_VUELOS_ANTES_DE_INSPECCION) {
            // Realizar inspección en el taller
            a.numeroDeVuelos = 0; // Resetear contador después de inspección
            System.out.println("El avión con ID: " + id + " está en inspección.");
            Thread.sleep(2000); // Simular tiempo de inspección
        }
    }

}