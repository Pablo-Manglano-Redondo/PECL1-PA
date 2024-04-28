package PrimeraParte;

import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JTextField;

public class Aerovia {
    
    private String idAerovia;
    private AtomicInteger avionesEnRuta = new AtomicInteger(0);
    private JTextField aerovia;

    public Aerovia(String idAerovia, JTextField aerovia) {
        this.idAerovia = idAerovia;
        this.aerovia = aerovia;
    }
    
    
    
    public void accederAerovia(String idAvion) {
        avionesEnRuta.incrementAndGet();
        System.out.println("Avión con ID " + idAvion + " usando aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
        aerovia.setText(idAvion);
    }

    public void liberarAerovia(String idAvion) {
        avionesEnRuta.decrementAndGet();
        System.out.println("Avión con ID " + idAvion + " ha dejado la aerovía " + idAerovia + " . Total aviones en ruta: " + avionesEnRuta.get());
        aerovia.setText(idAvion);
    }

    public String getId() {
        return idAerovia;
    }
    
}
