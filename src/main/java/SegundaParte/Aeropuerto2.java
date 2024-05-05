package SegundaParte;

import PrimeraParte.Aeropuerto;
import PrimeraParte.Aerovia;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Aeropuerto2 extends UnicastRemoteObject implements AeropuertoRemoto{
    
    private Aeropuerto aero;
    private Aerovia aerovia;
    public Aeropuerto2(Aeropuerto aero, Aerovia aerovia) throws RemoteException{
        this.aero = aero;
        this.aerovia = aerovia;
    }

    public int pasajerosAeropuerto() throws RemoteException {
        return aero.getPasajerosAeropuerto(aero);
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
    
    public String aerovia() throws RemoteException {
        return aerovia.getAerovia().obtenerLista();
    }
    
    public void setEstadoPista(int pistaId, boolean estado) {
        System.out.println("Aeropuerto 2");
        aero.setEstadoPistaAeropuerto(pistaId, estado);
    }
}
