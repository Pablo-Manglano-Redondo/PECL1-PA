package SegundaParte;

import PrimeraParte.Aeropuerto;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI extends Thread{
    
    private Aeropuerto a;

    public ServidorRMI(Aeropuerto a) {
        this.a = a;
    }
    
    public void run() {
        try{
            Aeropuerto2 aero = new Aeropuerto2(a);
            Registry r = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/OR", (Remote) aero);
        } catch(Exception e){
            System.err.println("Error del servidor: " + e.getMessage());
        }
    }  
}
