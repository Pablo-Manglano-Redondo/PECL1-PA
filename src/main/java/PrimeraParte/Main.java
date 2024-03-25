package PrimeraParte;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        Autobus autobus;
        Avion avion;
        for (int i = 0; i < 4; i++) {
            autobus = new Autobus(i);
            Thread.sleep(500 + (int)Math.random()*501);
            System.out.println(autobus.imprimirAutobus());
            autobus.start();
        }
        
        for (int i = 0; i < 8; i++) {
            avion = new Avion(i);
            Thread.sleep(1000 + (int)Math.random()*2001);
            System.out.println(avion.imprimirAvion(i));
            avion.start();
        }
        
    }
    
}
