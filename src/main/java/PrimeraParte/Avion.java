package PrimeraParte;

import java.util.Random;


public class Avion extends Thread{
    
    private String id, destino;
    private String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    
    public Avion(int numero) {
        Random rand = new Random();
        char letra1 = abecedario.charAt(rand.nextInt(abecedario.length()));
        char letra2 = abecedario.charAt(rand.nextInt(abecedario.length()));
        this.id = String.format("%c%c-%04d", letra1, letra2, numero);
        this.destino = (numero% 2 == 0)?"Madrid":"Barcelona";
    }
    public String imprimirAvion(int numero) {
        return "Avión con ID: " + this.id + " con destino: " + this.destino;
    }
}
