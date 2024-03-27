package PrimeraParte;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvolucionAeropuerto {
    private FileWriter escribir;
    private static final Lock lock = new ReentrantLock();
    private File f;

    public void escribirLog(String s){
        f = new File("EvolucionAeropuerto.txt");
        
        lock.lock();
        
            if(!f.exists()){
              try {
                f.createNewFile();
                escribir = new FileWriter("EvolucionAeropuerto.txt",true);
                escribir.write(s + "\n");
                escribir.close();
            } catch (IOException ex) {
                Logger.getLogger(GeneradorLogs.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
 
        lock.unlock();
            }  
            }else{
              try {
                
                escribir = new FileWriter("EvolucionAeropuerto.txt",true);
                escribir.write(s + "\n");
                escribir.close();
            } catch (IOException ex) {
                Logger.getLogger(EvolucionAeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
            
        lock.unlock();
            }      
        }         
    }
}
