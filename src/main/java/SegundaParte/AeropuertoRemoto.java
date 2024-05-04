package SegundaParte;

import PrimeraParte.ListaVehiculos;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AeropuertoRemoto extends Remote{
    
    public int pasajerosAeropuerto() throws RemoteException;
    public int avionesEnHangar() throws RemoteException;
    public int avionesEnTaller() throws RemoteException;
    public int avionesEnAreaEstacionamiento() throws RemoteException;
    public int avionesEnAreaRodaje() throws RemoteException;
    public String aerovia() throws RemoteException;
    
}
