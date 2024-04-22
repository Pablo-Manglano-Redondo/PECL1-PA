package PrimeraParte;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Aeropuerto {
    
    private String nombre;
    private List<PuertaEmbarque> puertas;
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private int ocupacionActual = 0; 
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    private int pasajerosAeropuerto = 0;
    private int pasajerosEnAvion = 0;
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
    }
    
    public Aeropuerto(String nombre, int cantidadPuertas) {
        this.nombre = nombre;
        this.puertas = new ArrayList<>();
        inicializarPuertas(cantidadPuertas);
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------- PUERTA EMBARQUE -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
    private void inicializarPuertas(int cantidadPuertas) {
        puertas.add(new PuertaEmbarque(1, PuertaEmbarque.TipoPuerta.EMBARQUE));
        puertas.add(new PuertaEmbarque(2, PuertaEmbarque.TipoPuerta.DESEMBARQUE));
        for (int i = 3; i <= cantidadPuertas; i++) {
            puertas.add(new PuertaEmbarque(i, PuertaEmbarque.TipoPuerta.MIXTA));
        }
    }

    public synchronized PuertaEmbarque obtenerPuertaEmbarque(Avion a) {
        for (PuertaEmbarque puerta : puertas) {
            if ((puerta.getTipo() == PuertaEmbarque.TipoPuerta.EMBARQUE || puerta.getTipo() == PuertaEmbarque.TipoPuerta.MIXTA) && puerta.intentarUsar()) {
                System.out.println("Avión con ID " + a.getAvionId() + " ha entrado en la puerta de embarque " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " del aeropuerto " + nombre);
                a.setPuerta(puerta);
                return puerta;
            }
        }
        return null;
    }

    public synchronized PuertaEmbarque obtenerPuertaDesembarque(Avion a) {
        for (PuertaEmbarque puerta : puertas) {
            if ((puerta.getTipo() == PuertaEmbarque.TipoPuerta.DESEMBARQUE || puerta.getTipo() == PuertaEmbarque.TipoPuerta.MIXTA) && puerta.intentarUsar()) {
                System.out.println("Avión con ID " + a.getAvionId() + " ha entrado en la puerta de desembarque " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " del aeropuerto " + nombre);
                a.setPuerta(puerta);
                return puerta;
            }
        }
        return null;
    }

    public void liberarPuerta(PuertaEmbarque puerta) {
        puerta.liberar();
    }
    
    public String getNombrePuerta() {
        return nombre;
    }
    
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AVIONES -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public synchronized void agregarAvion(Avion a) {
        aviones.add(a);
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
    
    public synchronized void embarcarPasajeros(Avion a, int capacidad) {
        int intentos = 0;
        while (intentos < 3 && pasajerosAeropuerto > 0 && pasajerosEnAvion < capacidad) {
            int pasajerosQueSeIntentanCoger = Math.min(pasajerosAeropuerto, capacidad - pasajerosEnAvion);
            pasajerosEnAvion += pasajerosQueSeIntentanCoger;
            pasajerosAeropuerto -= pasajerosQueSeIntentanCoger;
            System.out.println("En el avión con ID " + a.getAvionId() + " se han subido " + pasajerosEnAvion + " pasajeros");
            try {
                Thread.sleep((int) (Math.random()*42001) + 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }
            intentos++;
        }
        if (pasajerosEnAvion < capacidad) {
           System.out.println("La puerta de embarque del avión con ID " + a.getAvionId() + " se cierra. No se admiten más pasajeros"); 
           return;
        }
        try {
                Thread.sleep((int) (Math.random()*4001) + 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void desembarcarPasajeros(Avion a, int capacidad) {
        pasajerosAeropuerto += pasajerosEnAvion;
        System.out.println("Del avión con ID " + a.getAvionId() + " se han bajado " + pasajerosEnAvion + " pasajeros");
        pasajerosEnAvion = 0;
    }
    
    public synchronized void esperarPista(Avion a) {
        capacidadPistas.release();
        System.out.println("El avión con ID " + a.getAvionId() + " ha abandonado la pista");
    }
    public synchronized void salirPista(Avion a) {
        try {
            capacidadPistas.acquire();
            System.out.println("El avión con ID " + a.getAvionId() + " está esperando en la pista para despegar");
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }   
    }
    
    public void despegar(Avion a) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está realizando las últimas verificaciones antes del despegue");
            Thread.sleep((int)(Math.random() * 3001) + 1000);
            System.out.println("El avión con ID " + a.getAvionId() + " ha despegado con éxito");
            salirPista(a);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    public void accederAerovia(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El abvión con ID " + a.getAvionId() + " está accediendo a la aerovía hacia " + aero.getNombreAeropuerto());
            Thread.sleep((int)(Math.random()*9001) + 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void volar(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está volando hacia " + aero);
            Thread.sleep((int)(Math.random()*15001) + 15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    public void aterrizar(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está solicitando una pista para aterrizar");
            esperarPista(a);
            System.out.println("El avión con ID " + a.getAvionId() + " ha accedido a una pista y está aterrizando");
            Thread.sleep((int) (Math.random()*4001) + 1000);
            System.out.println("El avión con ID " + a.getAvionId() + " ha aterrizado");
            salirPista(a);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    
    public boolean solicitarPista() {
        
        return false;
    }
    
    public void darRodeo(Avion a) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está dando un rodeo antes solicitar pista nuevamente");
            Thread.sleep((int)(Math.random()* 4001) + 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    
    public synchronized void entrarPista(Avion a) throws InterruptedException {
        capacidadPistas.acquire();
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
   
   
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AUTOBUS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
            Thread.sleep(2000 + (int) (Math.random() * 3001));
        } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + " yendo hacia el aeropuerto " + aero.getNombreAeropuerto() + " con " + n + " pasajeros.");
            Thread.sleep(5000 + (int) (Math.random() * 5001));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------- PASAJEROS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public void bajarPasajeros(Aeropuerto aero) {
        // Simulación de que los pasajeros bajan en el aeropuerto
        System.out.println("Los pasajeros han bajado en el aeropuerto " + aero.getNombreAeropuerto());
    }
    
    public void esperarNuevosPasajeros(String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + "se encuentra en el aeropuerto " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
            Thread.sleep(2000 + (int) (Math.random() * 3001));
        } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }
    
    public synchronized void llegadaAeropuerto(int pasajeros, Aeropuerto aero) {
        pasajerosAeropuerto += pasajeros;
        System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto);
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        pasajerosAeropuerto -= pasajeros;
        System.out.println("Han salido " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto);
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
    
    
}