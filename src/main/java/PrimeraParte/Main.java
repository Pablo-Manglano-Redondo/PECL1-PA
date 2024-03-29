package PrimeraParte;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Autobus autobus;
        
        Aeropuerto madrid = new Aeropuerto("Madrid");
        Aeropuerto barcelona = new Aeropuerto("Barcelona");
               
        Compartida compartida = new Compartida();
        
        for (int i = 0; i < 4; i++) {
            autobus = new Autobus(i, compartida);
            Thread.sleep(500 + (int)Math.random()*501);
            autobus.start();
        }
        
        for (int i = 0; i < 8000; i++) {
            String id = Avion.generarIdAvion(i); 
            Aeropuerto aeropuertoOrigen = (i% 2 == 0) ? madrid : barcelona;
            Avion avion = new Avion(id, aeropuertoOrigen);
            avion.start();
            try {
                Thread.sleep(1000 + new Random().nextInt(2001)); // Intervalo aleatorio entre 1 y 3 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
 