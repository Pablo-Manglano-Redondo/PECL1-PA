package PrimeraParte;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        
        int numAviones = 8000;
        int numAutobuses = 4000;
        Aeropuerto madrid = new Aeropuerto("Aeropuerto de Madrid", 1000, 6, 4);
        Aeropuerto barcelona = new Aeropuerto("Aeropuerto de Barcelona", 1000, 6, 4);
        Aerovia aeroviaMadridBarcelona = new Aerovia("aeroviaMadridBarcelona");
        Aerovia aeroviaBarcelonaMadrid = new Aerovia("aeroviaBarcelonaMadrid");
        GeneradorHilos g = new GeneradorHilos(numAviones, numAutobuses, aeroviaMadridBarcelona, aeroviaBarcelonaMadrid, madrid, barcelona);
        g.start();
    }
}
 