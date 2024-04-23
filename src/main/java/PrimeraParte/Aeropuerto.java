package PrimeraParte;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Aeropuerto {
    
    private int ocupacionActual = 0; 
    //private int pasajerosAeropuerto = 0;
    private int pasajerosEnAvion = 0;
    private String nombre;
    private int pasajerosAeropuerto1 = 0;
    private int pasajerosAeropuerto2 = 0;
    private List<PuertaEmbarque> puertas;
    private Map<PuertaEmbarque.TipoPuerta, Queue<Avion>> colasDeEspera;
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    
    
    public Aeropuerto(String nombre, int cantidadPuertas) {
        this.nombre = nombre;
        this.puertas = new ArrayList<>();
        this.colasDeEspera = new HashMap<>();
        for (PuertaEmbarque.TipoPuerta tipo : PuertaEmbarque.TipoPuerta.values()) {
            colasDeEspera.put(tipo, new ArrayDeque<>());
        }
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

    public synchronized PuertaEmbarque obtenerPuertaEmbarque(Avion a, Aeropuerto aero) {
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.EMBARQUE);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.EMBARQUE).add(a);
        }
        embarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        return puerta;
    }

    public synchronized PuertaEmbarque obtenerPuertaDesembarque(Avion a, Aeropuerto aero) {
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.DESEMBARQUE);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.DESEMBARQUE).add(a);
        }
        desembarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        return puerta;
    }

    private PuertaEmbarque obtenerPuerta(Avion avion, PuertaEmbarque.TipoPuerta tipo) {
        for (PuertaEmbarque puerta : puertas) {
            if ((puerta.getTipo() == tipo || puerta.getTipo() == PuertaEmbarque.TipoPuerta.MIXTA) && puerta.intentarUsar()) {
                System.out.println("Avión con ID " + avion.getAvionId() + " ha entrado en la puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " del aeropuerto " + nombre);
                avion.setPuerta(puerta);
                return puerta;
            }
        }
        return null;
    }

    public void liberarPuerta(PuertaEmbarque puerta) {
        puerta.liberar();
        checkQueues(puerta);
    }

    private void checkQueues(PuertaEmbarque puerta) {
        Queue<Avion> queue = colasDeEspera.get(puerta.getTipo());
        if (!queue.isEmpty()) {
            Avion avion = queue.poll();
            if (avion != null) {
                obtenerPuerta(avion, puerta.getTipo());
            }
        }
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
    
    public synchronized void embarcarPasajeros(Avion a, int capacidad, Aeropuerto aero) {
        int intentos = 0;
        int pasajerosAeropuerto;

        // Determinar qué aeropuerto estamos tratando
        if (aero.getNombreAeropuerto().equals("Aeropuerto de Madrid")) {
            pasajerosAeropuerto = aero.getPasajerosAeropuerto1();
        } else {
            pasajerosAeropuerto = aero.getPasajerosAeropuerto2();
        }

        // Intentar embarcar pasajeros hasta que se complete la capacidad del avión o se agoten los intentos
        while (intentos < 3 && pasajerosAeropuerto > 0 && pasajerosEnAvion < capacidad) {
            // Calcular la cantidad de pasajeros que se pueden embarcar en este intento
            int pasajerosQueSeIntentanCoger = Math.min(pasajerosAeropuerto, capacidad - pasajerosEnAvion);

            // Actualizar los contadores de pasajeros
            pasajerosEnAvion += pasajerosQueSeIntentanCoger;
            pasajerosAeropuerto -= pasajerosQueSeIntentanCoger;

            // Mostrar el mensaje de embarque
            System.out.println("En el avión con ID " + a.getAvionId() + " se han subido " + pasajerosEnAvion + " pasajeros");

            // Esperar un tiempo aleatorio antes de admitir más pasajeros
            try {
                Thread.sleep((int) (Math.random() * 4001) + 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Incrementar el contador de intentos
            intentos++;
        }

        // Si no se completa la capacidad del avión, cerrar la puerta de embarque
        if (pasajerosEnAvion < capacidad) {
           System.out.println("La puerta de embarque del avión con ID " + a.getAvionId() + " se cierra. No se admiten más pasajeros"); 
        }

        // Esperar un tiempo aleatorio al final del embarque
        try {
            Thread.sleep((int) (Math.random() * 4001) + 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public synchronized void desembarcarPasajeros(Avion a, int capacidad, Aeropuerto aero) {
        int pasajerosAeropuerto;
        if (aero.getNombreAeropuerto().equals("Aeropuerto de Madrid")) {
            pasajerosAeropuerto = aero.getPasajerosAeropuerto1();
        } else {
            pasajerosAeropuerto = aero.getPasajerosAeropuerto2();
        }
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
    
    public void despegar(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está realizando las últimas verificaciones antes del despegue");
            Thread.sleep((int)(Math.random() * 3001) + 1000);
            esperarPista(a);
            System.out.println("El avión con ID " + a.getAvionId() + " ha accedido a una pista y está despegando.");
            entrarPista(a);
            System.out.println("El avión con ID " + a.getAvionId() + " ha despegado con éxito desde el " + aero.getNombreAeropuerto());
            salirPista(a);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    public void accederAerovia(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El abvión con ID " + a.getAvionId() + " está accediendo a la aerovía hacia el " + aero.getNombreAeropuerto());
            Thread.sleep((int)(Math.random()*9001) + 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void volar(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está volando hacia " + aero.getNombreAeropuerto());
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
            entrarPista(a);
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
            System.out.println("El avión con ID " + a.getAvionId() + " está dando un rodeo antes de solicitar pista nuevamente");
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

    public int getPasajerosAeropuerto1() {
        return pasajerosAeropuerto1;
    }

    public int getPasajerosAeropuerto2() {
        return pasajerosAeropuerto2;
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
        // SUMAR AL CONTADOR
    }
    
    public void esperarNuevosPasajeros(String id, Aeropuerto aero) {
        try {
            System.out.println("El autobús con ID " + id + " se encuentra en el aeropuerto " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
            Thread.sleep(2000 + (int) (Math.random() * 3001));
        } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }
    
    public synchronized void llegadaAeropuerto(int pasajeros, Aeropuerto aero) {
        if (aero.getNombreAeropuerto().equals("Aeropuerto de Madrid")) {
            pasajerosAeropuerto1 += pasajeros;
            System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto1);
        } else {
            pasajerosAeropuerto2 += pasajeros;
            System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto2);
        }
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        if (aero.getNombreAeropuerto().equals("Aeropuerto de Madrid")) {
            pasajerosAeropuerto1 -= pasajeros;
            System.out.println("Han salido " + pasajeros + " pasajeros del aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto1);
        } else {
            pasajerosAeropuerto2 -= pasajeros;
            System.out.println("Han salido " + pasajeros + " pasajeros del aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + pasajerosAeropuerto2);
        }
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
    
    
}