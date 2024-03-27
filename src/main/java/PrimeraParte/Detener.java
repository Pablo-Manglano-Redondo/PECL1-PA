package PrimeraParte;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Detener {
    
    private boolean detenido = false;

    public boolean isDetenido() {
        return detenido;
    }
    
    public void pausar(){
        
       detenido=true;
    }
    
    public synchronized void esperar(){
        while(detenido){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Detener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void reanudar(){
        detenido=false;
        notifyAll();  
    }
}
