package PrimeraParte;

import java.time.LocalTime;
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
    //Contadores para la segunda parte
    private int avionesEnHangar = 0;
    private int avionesEnTaller = 0;
    private int avionesEnAeraEstacionamiento = 0;
    private int avionesEnAeraRodaje = 0;

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
    private LocalTime horaActual;
 
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
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.EMBARQUE, PuertaEmbarque.TipoPuerta.MIXTA);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.EMBARQUE).add(a);
            ea.escribirLog(h + ":" + m + ":" + s + "--Avión " + a.getAvionId() + " en espera para una puerta de embarque del " + this.getNombreAeropuerto());
        } else {
            embarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        }
        return puerta;
    }

    public synchronized PuertaEmbarque obtenerPuertaDesembarque(Avion a, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        PuertaEmbarque puerta = obtenerPuerta(a, PuertaEmbarque.TipoPuerta.DESEMBARQUE, PuertaEmbarque.TipoPuerta.MIXTA);
        if (puerta == null) {
            colasDeEspera.get(PuertaEmbarque.TipoPuerta.DESEMBARQUE).add(a);
            ea.escribirLog(h + ":" + m + ":" + s + "--Avión " + a.getAvionId() + " en espera para una puerta de desembarque del " + this.getNombreAeropuerto());
        } else {
            desembarcarPasajeros(a, a.getCapacidadPasajeros(), aero);
        }
        return puerta;
    }

    private PuertaEmbarque obtenerPuerta(Avion avion, PuertaEmbarque.TipoPuerta tipoPrincipal, PuertaEmbarque.TipoPuerta tipoSecundario) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        for (PuertaEmbarque puerta : puertas) {
            if ((puerta.getTipo() == tipoPrincipal || puerta.getTipo() == tipoSecundario) && puerta.intentarUsar()) {
                ea.escribirLog(h + ":" + m + ":" + s + "--Avión con ID " + avion.getAvionId() + " ha entrado en la puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " en " + nombre);
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
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        puerta.liberar();
        ea.escribirLog(h + ":" + m + ":" + s + "--Puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + " liberada del " + this.getNombreAeropuerto());
        gates.get(puerta.getNumero() - 1).quitar(avion.getAvionId());
        checkQueues(puerta);       
    }

    private void checkQueues(PuertaEmbarque puerta) {
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.EMBARQUE);
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.DESEMBARQUE);
        revisarYAsignarPuertaDesdeCola(puerta, PuertaEmbarque.TipoPuerta.MIXTA);
    }
    
    
    private void revisarYAsignarPuertaDesdeCola(PuertaEmbarque puerta, PuertaEmbarque.TipoPuerta tipo) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        Queue<Avion> queue = colasDeEspera.get(tipo);
        while (!queue.isEmpty()) {
            Avion avion = queue.peek();  // Obtener sin remover
            if (obtenerPuerta(avion, tipo, PuertaEmbarque.TipoPuerta.MIXTA) != null) {
                queue.poll();  // Remover sólo si se asignó una puerta
                ea.escribirLog(h + ":" + m + ":" + s + "--Avión " + avion.getAvionId() + " ahora usando la puerta " + puerta.getNumero() + " del tipo " + puerta.getTipo() + ", en el " + this.getNombreAeropuerto());
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
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        for (int i = 0; i < pistasSem.length; i++) {
            if (estadoPistas[i] && pistasSem[i].tryAcquire()) {
                try {
                    a.setNumeroPista(i);
                    entrarPista(a,i);
                }
                catch (InterruptedException ex) {
                    System.out.println("Interrupción al entrar en pista");
                }
                ea.escribirLog(h + ":" + m + ":" + s + "--Se le ha asignado la pista " + i + " al avión con ID: " + a.getAvionId() + " en el " + this.getNombreAeropuerto());
                return i; // Devuelve el índice de la pista asignada.
            }
        }
        ea.escribirLog(h + ":" + m + ":" + s + "--No hay pistas disponibles para el avión con ID: " + a.getAvionId() + " en el " + this.getNombreAeropuerto());
        return -1; // No hay pistas disponibles.
    }

    public synchronized void entrarPista(Avion a, int indice) throws InterruptedException {
        pistas.get(indice).añadir(a.getAvionId());
    }
    
    public synchronized void salirPista(Avion a, int indice) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        if (indice >= 0 && indice < pistasSem.length) {
            pistasSem[indice].release();
            pistas.get(indice).quitar(a.getAvionId());
            ea.escribirLog(h + ":" + m + ":" + s + "--Se ha liberado la pista " + indice + 1 + " del " + this.getNombreAeropuerto());
        }
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " ha salido de la pista.");
    }

    public boolean[] getEstadoPistas() {
        return estadoPistas;
    }
    
    public void setEstadoPistaAeropuerto(int pistaId, boolean estado) {
        System.out.println("El metodo funciona bien");
        if (estadoPistas == null) {
            estadoPistas[pistaId] = true; 
        }
        this.estadoPistas[pistaId] = estado;
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AVIONES -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public synchronized void agregarAvion(Avion a) {
        aviones.add(a);
    }
    
    public synchronized void aparecerHangar(Avion a) throws InterruptedException{
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnHangar++;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " está en el hangar del " + this.getNombreAeropuerto());
        hangar.añadir(a.getAvionId());
        Thread.sleep(1000);
    }
    
    public synchronized void salirHangar(Avion a){
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnHangar--;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: "+ a.getAvionId() + " ha salido del hangar del " + this.getNombreAeropuerto());
        hangar.quitar(a.getAvionId());
    }
    
    public void entrarTaller(Avion a) throws InterruptedException {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        capacidadTaller.acquire();
        avionesEnTaller++;
        Thread.sleep(5000 + (int) (Math.random()*5001)); // Reposo inicial entre 5 y 10 segundos
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha entrado en el taller con ocupación  " + avionesEnTaller + " en el " + this.getNombreAeropuerto());
        taller.añadir(a.getAvionId());
    }
    
    public void salirTaller(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnTaller--;
        capacidadTaller.release();
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha salido del taller con ocupación " + avionesEnTaller + " en el " + this.getNombreAeropuerto());
        taller.quitar(a.getAvionId());
    }
    
    public synchronized void embarcarPasajeros(Avion a, int capacidad, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        int intentos = 0;
    
        int pasajerosAvion = aero.getPasajerosAeropuerto(aero);

        // Intentar embarcar pasajeros hasta que se complete la capacidad del avión o se agoten los intentos
        while (intentos < 3 && pasajerosAvion > 0 && pasajerosEnAvion < capacidad) {
            int pasajerosQueSeIntentanCoger = Math.min(pasajerosAvion, capacidad - pasajerosEnAvion);
            pasajerosEnAvion += pasajerosQueSeIntentanCoger;
            pasajerosAvion -= pasajerosQueSeIntentanCoger;
            ea.escribirLog(h + ":" + m + ":" + s + "--En el avión con ID " + a.getAvionId() + " se han subido " + pasajerosEnAvion + " pasajeros en el " + this.getNombreAeropuerto());
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
            ea.escribirLog(h + ":" + m + ":" + s + "--La puerta de embarque del avión con ID " + a.getAvionId() + " en el " + aero.getNombreAeropuerto() + " se cierra. No se admiten más pasajeros"); 
        }

        try {
            Thread.sleep((int) (Math.random() * 4001) + 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void desembarcarPasajeros(Avion a, int capacidad, Aeropuerto aero) {  
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        llegadaAeropuerto(capacidad, aero);
        ea.escribirLog(h + ":" + m + ":" + s + "--Del avión con ID " + a.getAvionId() + " se han bajado " + pasajerosEnAvion + " pasajeros en el " + aero.getNombreAeropuerto());
        pasajerosEnAvion = 0; 
    }
    
    public void darRodeo(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--Avión " + a.getAvionId() + " dando un rodeo, esperando disponibilidad de pista en el " + this.getNombreAeropuerto());
        try { 
            Thread.sleep(1000);  // Esperar 2 segundos antes de intentar de nuevo
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();  // Manejar correctamente la interrupción
            System.out.println("Avión " + a.getAvionId() + " interrumpido mientras daba un rodeo.");
        }
    }
    
    public void despegar(Avion a, Aeropuerto aero, int pista) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " está realizando las últimas verificaciones antes del despegue en el " + this.getNombreAeropuerto());
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " ha accedido a una pista y está despegando en el " + this.getNombreAeropuerto());
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " ha despegado con éxito desde el " + aero.getNombreAeropuerto()); 
        salirPista(a, pista);
    }
   
    public void volar(Avion a, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " está volando hacia " + aero.getNombreAeropuerto());
    }
    
    public void aterrizar(Avion a, Aeropuerto aero, int pista) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " está solicitando una pista para aterrizar en el " + this.getNombreAeropuerto());
        ea.escribirLog(h + ":"+ m + ":" + s + "--El avión con ID " + a.getAvionId() + " ha accedido a una pista y está aterrizando en el " + this.getNombreAeropuerto());
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID " + a.getAvionId() + " ha aterrizado en el " + this.getNombreAeropuerto());
        salirPista(a, pista); 
    }
    
    public synchronized void entrarEstacionamiento(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnAeraEstacionamiento++;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha entrado en el área de estacionamiento del " + this.getNombreAeropuerto());
        areaEstacionamiento.añadir(a.getAvionId());
    }
    
    public synchronized void salirEstacionamiento(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnAeraEstacionamiento--;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha salido en el área de estacionamiento del " + this.getNombreAeropuerto());
        areaEstacionamiento.quitar(a.getAvionId());
    }
    
    public synchronized void entrarRodaje(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnAeraRodaje++;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha entrado en el área de rodaje del " + this.getNombreAeropuerto());
        areaRodaje.añadir(a.getAvionId());
    }
    
    public synchronized void salirRodaje(Avion a) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnAeraRodaje--;
        ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " ha salido del área de rodaje del " + this.getNombreAeropuerto());
        areaRodaje.quitar(a.getAvionId());
    }
    
   public void revisarNecesidadDeInspeccion(Avion a) throws InterruptedException {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        if (a.getNumeroDeVuelos() >= a.getMAX_VUELOS_ANTES_DE_INSPECCION()) {
            // Realizar inspección en el taller
            entrarTaller(a);
            ea.escribirLog(h + ":" + m + ":" + s + "--El avión con ID: " + a.getAvionId() + " está en inspección en el " + this.getNombreAeropuerto());
            Thread.sleep(2000); // Simular tiempo de inspección
            salirTaller(a);
            a.setNumeroDeVuelos(0); // Resetear contador después de inspección
            }
    }
   

    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------- AUTOBUS -----------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------
   
   public void llegadaParadaCentro(String id, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--El autobús con ID " + id + " se encuentra en la parada del centro de " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
        transfersCiudad.añadir(id);
        transfersAeropuerto.quitar(id);
    }

    public void marchaAeropuerto(int n, String id, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        ea.escribirLog(h + ":" + m + ":" + s + "--El autobús con ID " + id + " yendo hacia el " + aero.getNombreAeropuerto() + " con " + n + " pasajeros.");
        transfersAeropuerto.añadir(id);
        transfersCiudad.quitar(id);
    }
    
    public void esperarNuevosPasajeros(String id, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        try {
            ea.escribirLog(h + ":" + m + ":" + s + "--El autobús con ID " + id + " se encuentra en el " + aero.getNombreAeropuerto() + " esperando nuevos pasajeros.");
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
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        aero.pasajerosAeropuerto += pasajeros;
        actualizarPasajeros();
        ea.escribirLog(h + ":" + m + ":" + s + "--Han entrado " + pasajeros + " pasajeros en el " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
    }
    
    public synchronized void salidaAeropuerto(int pasajeros, Aeropuerto aero) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        aero.pasajerosAeropuerto -= pasajeros;
        actualizarPasajeros();
        ea.escribirLog(h + ":" + m + ":" + s + "--Han salido " + pasajeros + " pasajeros del " + aero.getNombreAeropuerto() + " . Total de pasajeros: " + aero.pasajerosAeropuerto);
        numPasajerosAeropuerto.setText(Integer.toString(pasajerosAeropuerto));
    }

    public String getNombreAeropuerto() {
        return nombre;
    }
      
    public int getPasajerosAeropuerto(Aeropuerto aero) {
        return aero.pasajerosAeropuerto;
    }

    public int getAvionesEnHangar() {
        return avionesEnHangar;
    }

    public int getAvionesEnTaller() {
        return avionesEnTaller;
    }

    public int getAvionesEnAeraEstacionamiento() {
        return avionesEnAeraEstacionamiento;
    }

    public int getAvionesEnAeraRodaje() {
        return avionesEnAeraRodaje;
    }
    
    
}