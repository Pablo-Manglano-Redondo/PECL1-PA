package PrimeraParte;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        Autobus autobus;
        Avion avion;
        Compartida compartida = new Compartida();
        for (int i = 0; i < 4; i++) {
            autobus = new Autobus(i, compartida);
            Thread.sleep(500 + (int)Math.random()*501);
            System.out.println(autobus.imprimirAutobus());
            autobus.start();
        }
        
        for (int i = 0; i < 8; i++) {
            avion = new Avion(i, compartida);
            Thread.sleep(1000 + (int)Math.random()*2001);
            System.out.println(avion.imprimirAvion());
            avion.start();
        }
        
    }
    
}
