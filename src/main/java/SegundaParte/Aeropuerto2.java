package SegundaParte;

import PrimeraParte.Aeropuerto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Aeropuerto2 extends UnicastRemoteObject implements AeropuertoRemoto{
    
    private Aeropuerto aero;
    
    public Aeropuerto2(Aeropuerto aero) throws RemoteException{
        this.aero = aero;
    }
    
    public int avionesEnHangar() throws RemoteException{
        return aero.getAvionesEnHangar();       
    }
    
    public int avionesEnTaller() throws RemoteException{
        return aero.getAvionesEnTaller();
    }
    
    public int avionesEnAreaEstacionamiento() throws RemoteException{
        return aero.getAvionesEnAeraEstacionamiento();
    }
    
    public int avionesEnAreaRodaje() throws RemoteException{
        return aero.getAvionesEnAeraRodaje();
    }
}
