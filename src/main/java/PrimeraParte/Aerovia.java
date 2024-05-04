package PrimeraParte;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JTextField;

public class Aerovia {
    
    private String idAerovia;
    private AtomicInteger avionesEnRuta = new AtomicInteger(0);
    private ListaVehiculos aerovia;
    private EvolucionAeropuerto ea = new EvolucionAeropuerto();
    private LocalTime horaActual;
    public Aerovia(String idAerovia, JTextField aerovia) {
        this.idAerovia = idAerovia;
        this.aerovia = new ListaVehiculos(aerovia);
    }
    
    
    public void accederAerovia(String idAvion) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnRuta.incrementAndGet();
        ea.escribirLog(h + ":" + m + ":" + s + "--Avión con ID " + idAvion + " usando aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
        aerovia.añadir(idAvion);
    }

    public void liberarAerovia(String idAvion) {
        horaActual = LocalTime.now();
        int h = horaActual.getHour();
        int m = horaActual.getMinute();
        int s = horaActual.getSecond();
        avionesEnRuta.decrementAndGet();
        ea.escribirLog(h + ":" + m + ":" + s + "--Avión con ID " + idAvion + " ha dejado la aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
        aerovia.quitar(idAvion);
    }

    public ListaVehiculos getAerovia() {
        return aerovia;
    }

    public String getId() {
        return idAerovia;
    }
    
}
