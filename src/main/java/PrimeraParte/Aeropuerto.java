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
    private int pasajerosAeropuerto;
    private Semaphore[] pistasSem;
    private boolean[] estadoPistas;
    private List<PuertaEmbarque> puertas;
    private Map<PuertaEmbarque.TipoPuerta, Queue<Avion>> colasDeEspera;
    private Semaphore capacidadTaller = new Semaphore(20, true);
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private ArrayList<Avion> aviones = new ArrayList<>();
    private ListaVehiculos transfersAeropuerto;
    private ListaVehiculos transfersCiudad;
    private JTextField numPasajerosAeropuerto;
    private ListaVehiculos hangar;
    private ListaVehiculos taller;
    private ListaVehiculos areaEstacionamiento;
    private ListaVehiculos areaRodaje;
    private List<ListaVehiculos> pistas;
    private List<ListaVehiculos> gates;
    
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
        this.gates = new ArrayList<>();
        
        gates.add(new ListaVehiculos(gate1));
        gates.add(new ListaVehiculos(gate2));
        gates.add(new ListaVehiculos(gate3));
        gates.add(new ListaVehiculos(gate4));
        gates.add(new ListaVehiculos(gate5));
        gates.add(new ListaVehiculos(gate6));
        
        this.areaRodaje = new ListaVehiculos(areaRodaje);
        this.pasajerosAeropuerto = pasajerosAeropuerto;
        this.puertas = new ArrayList<>();
        this.colasDeEspera = new HashMap<>();
        this.estadoPistas = new boolean[cantidadPistas];
        this.pistas = new ArrayList<>();
        this.pistasSem = new Semaphore[cantidadPistas];
        
        pistas.add(new ListaVehiculos(pista1));
        pistas.add(new ListaVehiculos(pista2));
        pistas.add(new ListaVehiculos(pista3));
        pistas.add(new ListaVehiculos(pista4));
        
        for (int i = 0; i < cantidadPistas; i++) {
            pistasSem[i] = new Semaphore(1, true);  // true para fairness, asegurando el orden de llegada
            estadoPistas[i] = true;  // Todas las pistas inicialmente abiertas
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
                if (puerta.getNumero() - 1 < gates.size()) {
                    gates.get(puerta.getNumero() - 1).añadir(avion.getAvionId());
                }
                return puerta;
            }
        }
        return null;
    }

    public void liberarPuerta(PuertaEmbarque puerta, Avion avion) {
        puerta.liberar();
        System.out.println("Puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " liberada del aeropuerto " + this.getNombreAeropuerto());
        gates.get(puerta.getNumero() - 1).quitar(avion.getAvionId());
        checkQueues(puerta);       
    }

    private void checkQueues(PuertaEmbarque puerta) {
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.EMBARQUE);
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.DESEMBARQUE);
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.MIXTA);
    }
    
    
    private void revisarYAsignarPuertaDesdeCola(PuertaEmbarque puerta, PuertaEmbarque.TipoPuerta tipo) {
        Queue<Avion> queue = colasDeEspera.get(tipo);
        while (!queue.isEmpty()) {
            Avion avion = queue.peek();  // Obtener sin remover
            if (obtenerPuerta(avion, tipo, PuertaEmbarque.TipoPuerta.MIXTA) != null) {
                queue.poll();  // Remover sólo si se asignó una puerta
                System.out.println("Avión " + avion.getAvionId() + " ahora usando la puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + ".");
                break;  // Salir después de asignar la puerta para evitar asignar múltiples aviones a una sola puerta liberada
            }
        }
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
        for (int i = 0; i < pistasSem.length; i++) {
            if (estadoPistas[i] && pistasSem[i].tryAcquire()) {
                try {
                    a.setNumeroPista(i);
                    entrarPista(a,i);
                }
                catch (InterruptedException ex) {
                    System.out.println("Interrupción al entrar en pista");
                }
                System.out.println("Se le ha asignado la pista " + i + " al avión con ID: " + a.getAvionId() + " en el aeropuerto " + this.getNombreAeropuerto());
                return i; // Devuelve el índice de la pista asignada.
            }
        }
        System.out.println("No hay pistas disponibles para el avión con ID: " + a.getAvionId());
        return -1; // No hay pistas disponibles.
    }

    public synchronized void entrarPista(Avion a, int indice) throws InterruptedException {
        pistas.get(indice).añadir(a.getAvionId());
    }
    
    public synchronized void salirPista(Avion a, int indice) {
        if (indice >= 0 && indice < pistasSem.length) {
            pistasSem[indice].release();
            pistas.get(indice).quitar(a.getAvionId());
            System.out.println("Se ha liberado la pista " + indice + " del aeropuerto " + this.getNombreAeropuerto());
        }
        System.out.println("El avión con ID " + a.getAvionId() + " ha salido de la pista.");
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AVIONES -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public synchronized void agregarAvion(Avion a) {
        aviones.add(a);
    }
    
    public synchronized void aparecerHangar(Avion a) throws InterruptedException{
        ea.escribirLog("El avión con ID: "+ a.getAvionId() + " está en el hangar.");
        System.out.println("El avión con ID: " + a.getAvionId() + " está en el hangar");
        
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
    
    public void despegar(Avion a, Aeropuerto aero, int pista) {
        System.out.println("El avión con ID " + a.getAvionId() + " está realizando las últimas verificaciones antes del despegue");
        System.out.println("El avión con ID " + a.getAvionId() + " ha accedido a una pista y está despegando.");
        System.out.println("El avión con ID " + a.getAvionId() + " ha despegado con éxito desde el " + aero.getNombreAeropuerto());
        salirPista(a, pista);
    }
   
    public void volar(Avion a, Aeropuerto aero) {
        System.out.println("El avión con ID " + a.getAvionId() + " está volando hacia " + aero.getNombreAeropuerto());
    }
    
    public void aterrizar(Avion a, Aeropuerto aero, int pista) {
        System.out.println("El avión con ID " + a.getAvionId() + " está solicitando una pista para aterrizar");
        System.out.println("El avión con ID " + a.getAvionId() + " ha accedido a una pista y está aterrizando");
        System.out.println("El avión con ID " + a.getAvionId() + " ha aterrizado");
        salirPista(a, pista); 
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
   
    
    
    
    
    
    
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AUTOBUS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
        transfersCiudad.añadir(id);
        transfersAeropuerto.quitar(id);
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        System.out.println("El autobús con ID " + id + " yendo hacia el aeropuerto " + aero.getNombreAeropuerto() + " con " + n + " pasajeros.");
        transfersAeropuerto.añadir(id);
        transfersCiudad.quitar(id);
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
    
    public synchronized void actualizarPasajeros() {
        int numeroPasajeros = this.getPasajerosAeropuerto(this);
        numPasajerosAeropuerto.setText(Integer.toString(numeroPasajeros));
    }
    
    public synchronized void llegadaAeropuerto(int pasajeros, Aeropuerto aero) {
        aero.pasajerosAeropuerto += pasajeros;
        actualizarPasajeros();
        System.out.println("Han entrado " + pasajeros + " pasajeros en el aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        aero.pasajerosAeropuerto -= pasajeros;
        actualizarPasajeros();
        System.out.println("Han salido " + pasajeros + " pasajeros del aeropuerto " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
        numPasajerosAeropuerto.setText(Integer.toString(pasajerosAeropuerto));
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
      
    public int getPasajerosAeropuerto(Aeropuerto aero) {
        return aero.pasajerosAeropuerto;
    }
}