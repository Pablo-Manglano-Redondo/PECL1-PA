package SegundaParte;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AeropuertoRemoto extends Remote{
    
    public int avionesEnHangar() throws RemoteException;
    public int avionesEnTaller() throws RemoteException;
    public int avionesEnAreaEstacionamiento() throws RemoteException;
    public int avionesEnAreaRodaje() throws RemoteException;
    
    
}
