package PrimeraParte;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        
        int numAviones = 4;
        int numAutobuses = 1;
        Aeropuerto madrid = new Aeropuerto("Aeropuerto de Madrid", 6);
        Aeropuerto barcelona = new Aeropuerto("Aeropuerto de Barcelona", 6);
        Aerovia aeroviaMadridBarcelona = new Aerovia("aeroviaMadridBarcelona");
        Aerovia aeroviaBarcelonaMadrid = new Aerovia("aeroviaBarcelonaMadrid");
        GeneradorHilos g = new GeneradorHilos(numAviones, numAutobuses, aeroviaMadridBarcelona, aeroviaBarcelonaMadrid, madrid, barcelona);
        g.start();
    }
}
 