package PrimeraParte;


public class Autobus extends Thread {
    
    private String id, destino;

    public Autobus(int numero) {
        this.id = String.format("B-%04d", numero);
        this.destino = (numero % 2 == 0)?"Madrid":"Barcelona";
    }
    public String imprimirAutobus() {
        return "Autobús con ID: " + this.id + " creado, con destino: " + this.destino;
    }
    
}
