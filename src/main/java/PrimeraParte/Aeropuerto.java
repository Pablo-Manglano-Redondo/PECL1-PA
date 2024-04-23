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


public class Aeropuerto extends Thread{
    
    private int ocupacionActual = 0; 
    private int pasajerosEnAvion = 0;
    private String nombre;
    private int pasajerosAeropuerto = 0;
    private List<PuertaEmbarque> puertas;
    private Map<PuertaEmbarque.TipoPuerta, Queue<Avion>> colasDeEspera;
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    
    
    public Aeropuerto(String nombre, int pasajerosAeropuerto, int cantidadPuertas) {
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
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.EMBARQUE, PuertaEmbarque.TipoPuerta.MIXTA);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.EMBARQUE).add(a);
            System.out.println("Avión " + a.getAvionId() + " en espera para una puerta de embarque.");
        } else {
            embarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        }
        return puerta;
    }

    public synchronized PuertaEmbarque obtenerPuertaDesembarque(Avion a, Aeropuerto aero) {
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.DESEMBARQUE, PuertaEmbarque.TipoPuerta.MIXTA);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.DESEMBARQUE).add(a);
            System.out.println("Avión " + a.getAvionId() + " en espera para una puerta de desembarque.");
        } else {
            desembarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        }
        return puerta;
    }

    private PuertaEmbarque obtenerPuerta(Avion avion, PuertaEmbarque.TipoPuerta tipoPrincipal, PuertaEmbarque.TipoPuerta tipoSecundario) {
        for (PuertaEmbarque puerta : puertas) {
            if ((puerta.getTipo() == tipoPrincipal || puerta.getTipo() == tipoSecundario) && puerta.intentarUsar()) {
                System.out.println("Avión con ID " + avion.getAvionId() + " ha entrado en la puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " en " + nombre);
                avion.setPuerta(puerta);
                return puerta;
            }
        }
        return null;
    }

    public void liberarPuerta(PuertaEmbarque puerta) {
        puerta.liberar();
        System.out.println("Puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " liberada.");
        checkQueues(puerta);
    }

    private void checkQueues(PuertaEmbarque puerta) {
        // Revisar la cola de la misma tipo de puerta
        revisarYAsignarPuertaDesdeCola(puerta, puerta.getTipo());

        // Si la puerta es mixta, también podría ser necesaria para el otro tipo de operación
        if (puerta.getTipo() == PuertaEmbarque.TipoPuerta.MIXTA) {
            PuertaEmbarque.TipoPuerta otroTipo = (puerta.getTipo() == PuertaEmbarque.TipoPuerta.EMBARQUE) ? PuertaEmbarque.TipoPuerta.DESEMBARQUE : PuertaEmbarque.TipoPuerta.EMBARQUE;
            revisarYAsignarPuertaDesdeCola(puerta, otroTipo);
        }
    }

    private void revisarYAsignarPuertaDesdeCola(PuertaEmbarque puerta, PuertaEmbarque.TipoPuerta tipo) {
        Queue<Avion> queue = colasDeEspera.get(tipo);
        if (!queue.isEmpty()) {
            Avion avion = queue.poll();
            if (avion != null) {
                PuertaEmbarque asignada = obtenerPuerta(avion, tipo, PuertaEmbarque.TipoPuerta.MIXTA);
                if (asignada != null) {
                    System.out.println("Avión " + avion.getAvionId() + " ahora usando la puerta " + asignada.getNumero() + " del tipo " + asignada.getTipo() + ".");
                } else {
                    System.out.println("No se pudo reasignar una puerta a avión " + avion.getAvionId() + " después de liberar puerta.");
                    // Si no se pudo asignar, reintroducir el avión a la cola para esperar otra oportunidad.
                    colasDeEspera.get(tipo).add(avion);
                }
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
    
        int pasajerosAvion = aero.getPasajerosAeropuerto(aero);

        // Intentar embarcar pasajeros hasta que se complete la capacidad del avión o se agoten los intentos
        while (intentos < 3 && pasajerosAvion > 0 && pasajerosEnAvion < capacidad) {
            int pasajerosQueSeIntentanCoger = Math.min(pasajerosAvion, capacidad - pasajerosEnAvion);
            pasajerosEnAvion += pasajerosQueSeIntentanCoger;
            pasajerosAvion -= pasajerosQueSeIntentanCoger;
            System.out.println("En el avión con ID " + a.getAvionId() + " se han subido " + pasajerosEnAvion + " pasajeros");

            try {
                Thread.sleep((int) (Math.random() * 4001) + 1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

            intentos++;
        }

        if (pasajerosEnAvion == capacidad) {
            System.out.println("La puerta de embarque del avión con ID " + a.getAvionId() + " en el aeropuerto " + aero.getNombreAeropuerto() + " se cierra. No se admiten más pasajeros"); 
        }

        try {
            Thread.sleep((int) (Math.random() * 4001) + 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void desembarcarPasajeros(Avion a, int capacidad, Aeropuerto aero) {   
        llegadaAeropuerto(capacidad, aero);
        System.out.println("Del avión con ID " + a.getAvionId() + " se han bajado " + pasajerosEnAvion + " pasajeros en el " + aero.getNombreAeropuerto());
        pasajerosEnAvion = 0;
        
    }
    
    public synchronized void esperarPista(Avion a) {
        capacidadPistas.release();
        System.out.println("El avión con ID " + a.getAvionId() + " está esperando la pista");
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
        System.out.println("El avión con ID " + a.getAvionId() + " está accediendo a la aerovía hacia el " + aero.getNombreAeropuerto());
    }
    public void volar(Avion a, Aeropuerto aero) {
        System.out.println("El avión con ID " + a.getAvionId() + " está volando hacia " + aero.getNombreAeropuerto());
    }
    public void aterrizar(Avion a, Aeropuerto aero) {
        try {
            System.out.println("El avión con ID " + a.getAvionId() + " está solicitando una pista para aterrizar");
            esperarPista(a);
            System.out.println("El avión con ID " + a.getAvionId() + " ha accedido a una pista y está aterrizando");
            entrarPista(a);
            System.out.println("El avión con ID " + a.getAvionId() + " ha aterrizado");
            salirPista(a);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    
    public boolean solicitarPista() {
        return capacidadPistas.tryAcquire();
    }

    
    public void darRodeo(Avion a) {
        System.out.println("Avión " + a.getAvionId() + " dando un rodeo, esperando disponibilidad de pista.");
        try {
            Thread.sleep(2000);  // Esperar 2 segundos antes de intentar de nuevo
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();  // Manejar correctamente la interrupción
            System.out.println("Avión " + a.getAvionId() + " interrumpido mientras daba un rodeo.");
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

    public int getPasajerosAeropuerto(Aeropuerto aero) {
        return aero.pasajerosAeropuerto;
    }
   
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AUTOBUS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " yendo hacia el aeropuerto " + aero.getNombreAeropuerto() + " con " + n + " pasajeros.");
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
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------- PASAJEROS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public synchronized void llegadaAeropuerto(int pasajeros, Aeropuerto aero) {
        aero.pasajerosAeropuerto += pasajeros;
        System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        aero.pasajerosAeropuerto -= pasajeros;
        System.out.println("Han salido " + pasajeros + " pasajeros del aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
    
    
}