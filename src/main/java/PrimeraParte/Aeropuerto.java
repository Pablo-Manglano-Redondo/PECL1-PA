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
import javax.swing.JTextField;


public class Aeropuerto {
    
    private int ocupacionActual = 0; 
    private int pasajerosEnAvion = 0;
    private String nombre;
    private int pasajerosAeropuerto = 0;
    private int NUMERO_PISTAS = 4;
    private Semaphore[] pistas;
    private boolean[] estadoPistas;
    private List<PuertaEmbarque> puertas;
    private Map<PuertaEmbarque.TipoPuerta, Queue<Avion>> colasDeEspera;
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private Semaphore capacidadPistas = new Semaphore(4, true);
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    private ListaVehiculos transfersAeropuerto;
    private ListaVehiculos transfersCiudad;
    private JTextField numPasajerosAeropuerto;
    private ListaVehiculos hangar;
    private ListaVehiculos taller;
    private ListaVehiculos areaEstacionamiento;
    private ListaVehiculos gate1;
    private ListaVehiculos gate2;
    private ListaVehiculos gate3;
    private ListaVehiculos gate4;
    private ListaVehiculos gate5;
    private ListaVehiculos gate6;
    private ListaVehiculos areaRodaje;
    private ListaVehiculos pista1;
    private ListaVehiculos pista2;
    private ListaVehiculos pista3;
    private ListaVehiculos pista4;
            
    
   /* public Aeropuerto(String nombre, int pasajerosAeropuerto, int cantidadPuertas, int cantidadPistas) {
        this.nombre = nombre;
        this.pasajerosAeropuerto = pasajerosAeropuerto;
        this.puertas = new ArrayList<>();
        this.colasDeEspera = new HashMap<>();
        this.estadoPistas = new boolean[cantidadPistas];
        this.pistas = new Semaphore[cantidadPistas];
        for (int i = 0; i < cantidadPistas; i++) {
            this.pistas[i] = new Semaphore(1);
            this.estadoPistas[i] = true;
        }
        for (PuertaEmbarque.TipoPuerta tipo : PuertaEmbarque.TipoPuerta.values()) {
            colasDeEspera.put(tipo, new ArrayDeque<>());
        }
        inicializarPuertas(cantidadPuertas);
    }*/
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------- CONSTRUCTOR CON LOS JTEXTFIELDS PARA CONECTAR CON LA INTERFAZ -------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------

    public Aeropuerto(String nombre,  int pasajerosAeropuerto, int cantidadPuertas, int cantidadPistas, JTextField transfersAeropuerto, JTextField transfersCiudad, JTextField numPasajerosAeropuerto, JTextField hangar, 
            JTextField taller, JTextField areaEstacionamiento, JTextField gate1, JTextField gate2, JTextField gate3, JTextField gate4, JTextField gate5, JTextField gate6, 
            JTextField areaRodaje, JTextField pista1, JTextField pista2, JTextField pista3, JTextField pista4) {
        this.nombre = nombre;
        this.transfersAeropuerto = new ListaVehiculos(transfersAeropuerto);
        this.transfersCiudad = new ListaVehiculos(transfersCiudad);
        this.numPasajerosAeropuerto = numPasajerosAeropuerto;
        this.hangar = new ListaVehiculos(hangar);
        this.taller = new ListaVehiculos(taller);
        this.areaEstacionamiento = new ListaVehiculos(areaEstacionamiento);
        this.gate1 = new ListaVehiculos(gate1);
        this.gate2 = new ListaVehiculos(gate2);
        this.gate3 = new ListaVehiculos(gate3);
        this.gate4 = new ListaVehiculos(gate4);
        this.gate5 = new ListaVehiculos(gate5);
        this.gate6 = new ListaVehiculos(gate6);
        this.areaRodaje = new ListaVehiculos(areaRodaje);
        this.pista1 = new ListaVehiculos(pista1);
        this.pista2 = new ListaVehiculos(pista2);
        this.pista3 = new ListaVehiculos(pista3);
        this.pista4 = new ListaVehiculos(pista4);
        this.pasajerosAeropuerto = pasajerosAeropuerto;
        this.puertas = new ArrayList<>();
        this.colasDeEspera = new HashMap<>();
        this.estadoPistas = new boolean[cantidadPistas];
        this.pistas = new Semaphore[cantidadPistas];
        for (int i = 0; i < cantidadPistas; i++) {
            this.pistas[i] = new Semaphore(1);
            this.estadoPistas[i] = true;
        }
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
                gate1.añadir(avion.getAvionId());
                gate2.añadir(avion.getAvionId());
                gate3.añadir(avion.getAvionId());
                gate4.añadir(avion.getAvionId());
                gate5.añadir(avion.getAvionId());
                gate6.añadir(avion.getAvionId());
                return puerta;
            }
        }
        return null;
    }

    public void liberarPuerta(PuertaEmbarque puerta) {
        puerta.liberar();
        System.out.println("Puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " liberada del aeropuerto " + this.getNombreAeropuerto());
        checkQueues(puerta);
    }

    private void checkQueues(PuertaEmbarque puerta) {
        revisarYAsignarPuertaDesdeCola(puerta, puerta.getTipo());
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
    
    public synchronized void entrarHangar(Avion a) throws InterruptedException{
        // Ya está terminado
        ea.escribirLog("El avión con ID: "+ a.getAvionId() + " ha entrado en el hangar.");
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el hangar");
        
        hangar.añadir(a.getAvionId());
        Thread.sleep(1000);
    }
    
    public synchronized void salirHangar(Avion a){
        // Ya está terminado
        ea.escribirLog("El avión con ID: "+ a.getAvionId() + " ha salido del hangar.");
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del hangar");
        hangar.quitar(a.getAvionId());
    }
    
    public void entrarTaller(Avion a) throws InterruptedException {
        capacidadTaller.acquire();
        ocupacionActual++;
        Thread.sleep(5000 + (int) (Math.random()*5001)); // Reposo inicial entre 5 y 10 segundos
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el taller con ocupación  " + ocupacionActual);
        taller.añadir(a.getAvionId());
    }
    
    public void salirTaller(Avion a) {
        ocupacionActual--;
        capacidadTaller.release();
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del taller con ocupación " + ocupacionActual);
        taller.quitar(a.getAvionId());
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
    
    public void darRodeo(Avion a) {
        System.out.println("Avión " + a.getAvionId() + " dando un rodeo, esperando disponibilidad de pista.");
        try {
            Thread.sleep(1000);  // Esperar 2 segundos antes de intentar de nuevo
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();  // Manejar correctamente la interrupción
            System.out.println("Avión " + a.getAvionId() + " interrumpido mientras daba un rodeo.");
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
    
    public synchronized void entrarEstacionamiento(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el área de estacionamiento");
        areaEstacionamiento.añadir(a.getAvionId());
    }
    
    public synchronized void salirEstacionamiento(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido en el área de estacionamiento");
        areaEstacionamiento.quitar(a.getAvionId());
    }
    
    public synchronized void entrarRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha entrado en el área de rodaje");
        areaRodaje.añadir(a.getAvionId());
    }
    
    public synchronized void salirRodaje(Avion a) {
        System.out.println("El avión con ID: " + a.getAvionId() + " ha salido del área de rodaje");
        areaRodaje.quitar(a.getAvionId());
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
    // ----------------------------------------------------------------- PISTAS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public void abrirCerrarPista(int indice, boolean abrir) {
        if (indice >= 0 && indice < estadoPistas.length) {
            estadoPistas[indice] = abrir;
        }
    }
    
    public int solicitarPista(Avion a) {
        for (int i = 0; i < pistas.length; i++) {
            if (estadoPistas[i] && pistas[i].tryAcquire()) {
                // La pista está abierta y disponible para ser usada.
                System.out.println("Se le ha asignado la pista " + i + " al avión con ID: " + a.getAvionId() + " en el aeropuerto " + this.getNombreAeropuerto());
                return i; // Devuelve el índice de la pista asignada.
            }
        }
        return -1; // No hay pistas disponibles.
    }
    
    public void liberarPista(int indice) {
        if (indice >= 0 && indice < pistas.length) {
            pistas[indice].release();
            System.out.println("Se ha liberado la pista " + indice + " del aeropuerto " + this.getNombreAeropuerto());
        }
    }
    
    public synchronized void entrarPista(Avion a) throws InterruptedException {
        capacidadPistas.acquire();
        pista1.añadir(a.getAvionId());
        pista2.añadir(a.getAvionId());
        pista3.añadir(a.getAvionId());
        pista4.añadir(a.getAvionId());
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
        pista1.quitar(a.getAvionId());
        pista2.quitar(a.getAvionId());
        pista3.quitar(a.getAvionId());
        pista4.quitar(a.getAvionId());
    }
   
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AUTOBUS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
        transfersCiudad.añadir(id);
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " yendo hacia el aeropuerto " + aero.getNombreAeropuerto() + " con " + n + " pasajeros.");
        transfersAeropuerto.quitar(id);
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
        numPasajerosAeropuerto.setText(Integer.toString(pasajeros));
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        aero.pasajerosAeropuerto -= pasajeros;
        System.out.println("Han salido " + pasajeros + " pasajeros del aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
        numPasajerosAeropuerto.setText(Integer.toString(pasajeros));
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
    
    
}