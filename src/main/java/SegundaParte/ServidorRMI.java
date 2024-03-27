package SegundaParte;

import PrimeraParte.Compartida;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI extends Thread{
    
    private Compartida c;
    

    public ServidorRMI(Compartida c) {
        this.c = c;
       
    }
    
    public void run() {
        try{
            Compartida2 cr = new Compartida2(c);
            Registry r = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/OR", cr);
        } catch(Exception e){
            System.err.println("Error del servidor: " + e.getMessage());
        }
    }  
}
