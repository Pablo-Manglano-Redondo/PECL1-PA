package PrimeraParte;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class EvolucionAeropuerto {
    private static final Lock lock = new ReentrantLock();
    
    public static void escribirLog(String mensaje) {
        lock.lock();
        try {
            // Lógica para escribir en el log
        } finally {
            lock.unlock();
        }
    }
}
