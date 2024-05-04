package SegundaParte;

import PrimeraParte.Aeropuerto;
import PrimeraParte.Aerovia;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI extends Thread{
    
    private Aeropuerto a;
    private int puerto;
    private String l;
    private Aerovia aerovia;
    public ServidorRMI(Aeropuerto a, int puerto, String l, Aerovia aerovia) {
        this.a = a;
        this.puerto=puerto;
        this.l = l;
        this.aerovia = aerovia;
    }
    
    public void run() {
        try{
            Aeropuerto2 aero = new Aeropuerto2(a, aerovia);
            Registry r = LocateRegistry.createRegistry(puerto);
            Naming.rebind("//localhost/OR" + l, (Remote) aero);
        } catch(Exception e){
            System.err.println("Error del servidor: " + e.getMessage());
        }
    }  
    
}
