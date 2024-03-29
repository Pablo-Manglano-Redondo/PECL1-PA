package PrimeraParte;

import java.util.concurrent.Semaphore;


public class Aeropuerto {
    
    private String nombre;
    private Semaphore esperarPuertaEmbarque = new Semaphore(6, true);
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private int ocupacionActual = 0; 
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
    }
    
    public synchronized void actualizarPasajeros(/* parámetros */) {
        // Lógica para actualizar el número de pasajeros
    }
    
    public synchronized void entrarHangar(Avion a){
        // Ya está terminado
        ea.escribirLog("El avión con ID: "+ a.getAvionId() + " ha entrado en el hangar.");
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el hangar");
    }
    
    public synchronized void salirHangar(Avion a){
        // Ya está terminado
        ea.escribirLog("El avión con ID: "+ a.getAvionId() + " ha salido del hangar.");
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del hangar");
    }
    
    public void entrarTaller(Avion a) throws InterruptedException {
        capacidadTaller.acquire();
        ocupacionActual++;
        Thread.sleep(5000 + (int) (Math.random()*5001)); // Reposo inicial entre 5 y 10 segundos
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el taller con ocupación  " + ocupacionActual);
    }
    
    public void salirTaller(Avion a) {
        ocupacionActual--;
        capacidadTaller.release();
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del taller con ocupación " + ocupacionActual);
    }
    
    public void entrarPuertaEmbarque(Avion a) throws InterruptedException {
        esperarPuertaEmbarque.acquire();
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado por la puerta de embarque");
        
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
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el área de estacionamiento");
    }
    
    public synchronized void salirEstacionamiento(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido en el área de estacionamiento");
    }
    
    public synchronized void entrarRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el área de rodaje");
    }
    
    public synchronized void salirRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del área de rodaje");
    }
    
   public void revisarNecesidadDeInspeccion(Avion a) throws InterruptedException {
       
    if (a.getNumeroDeVuelos() >= a.getMAX_VUELOS_ANTES_DE_INSPECCION()) {
        // Realizar inspección en el taller
        entrarTaller(a);
        System.out.println("El avión con ID: " + a.getAvionId() + " está en inspección.");
        Thread.sleep(2000); // Simular tiempo de inspección
        salirTaller(a);
        a.setNumeroDeVuelos(0); // Resetear contador después de inspección
        }
    }
}