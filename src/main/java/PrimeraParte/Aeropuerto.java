package PrimeraParte;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Aeropuerto {
    
    private String nombre;
    private Semaphore esperarPuertaEmbarque = new Semaphore(6, true);
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private int ocupacionActual = 0; 
    private int pasajerosAeropuerto = 0;
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
    }
    
    public synchronized void agregarAvion(Avion a) {
        aviones.add(a);
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
    public void esperarPuertaEmbarque(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " está esperando en la puerta de embarque");
    }
    
    public void embarcarPasajeros() {
        System.out.println("Los pasajeros han embarcado");
    }
    
    public void desembarcarPasajeros() {
        System.out.println("Los pasajeros han desembarcado");
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
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombre() + " esperando nuevos pasajeros.");
            Thread.sleep(2000 + (int) (Math.random() * 3001));
        } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + " yendo hacia el aeropuerto " + aero.getNombre() + " con " + n + " pasajeros.");
            Thread.sleep(5000 + (int) (Math.random() * 5001));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    public void bajarPasajeros(Aeropuerto aero) {
        // Simulación de que los pasajeros bajan en el aeropuerto
        System.out.println("Los pasajeros han bajado en el aeropuerto " + aero.getNombre());
    }
    
    public void esperarNuevosPasajeros(String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + "se encuentra en el aeropuerto " + aero.getNombre() + " esperando nuevos pasajeros.");
            Thread.sleep(2000 + (int) (Math.random() * 3001));
        } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }
    
    public synchronized void llegadaAeropuerto(int pasajeros, Aeropuerto aero) {
        pasajerosAeropuerto += pasajeros;
        System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombre() + " . Total de pasajeros: " + pasajerosAeropuerto);
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        pasajerosAeropuerto -= pasajeros;
        System.out.println("Han salido " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombre() + " . Total de pasajeros: " + pasajerosAeropuerto);
    }

    public String getNombre() {
        return nombre;
    }
    
    
}