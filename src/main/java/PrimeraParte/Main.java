package PrimeraParte;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        
        int numAviones = 8000;
        int numAutobuses = 4000;
        GeneradorHilos g = new GeneradorHilos(numAviones, numAutobuses);
        g.start();
    }
}
 