package PrimeraParte;

public class PuertaEmbarque {
    public enum TipoPuerta {
        EMBARQUE, DESEMBARQUE, MIXTA
    }

    private int numero;
    private boolean disponible;
    private TipoPuerta tipo;

    public PuertaEmbarque(int numero, TipoPuerta tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.disponible = true;
    }

    public synchronized boolean intentarUsar() {
        if (disponible) {
            disponible = false;
            return true;
        }
        return false;
    }

    public synchronized void liberar() {
        disponible = true;
    }

    public int getNumero() {
        return numero;
    }

    public TipoPuerta getTipo() {
        return tipo;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
