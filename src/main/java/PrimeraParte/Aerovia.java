package PrimeraParte;

import java.util.concurrent.atomic.AtomicInteger;

public class Aerovia {
    
    private String idAerovia;
    private AtomicInteger avionesEnRuta = new AtomicInteger(0);

    public Aerovia(String idAerovia) {
        this.idAerovia = idAerovia;
    }
    
    public void accederAerovia(String idAvion) {
        avionesEnRuta.incrementAndGet();
        System.out.println("Avión con ID " + idAvion + " usando aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
    }

    public void liberarAerovia(String idAvion) {
        avionesEnRuta.decrementAndGet();
        System.out.println("Avión con ID " + idAvion + " ha dejado la aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
    }

    public String getId() {
        return idAerovia;
    }
    
}
