package PrimeraParte;

public class Autobus extends Thread {
    private final String id;
    private final String destino;
    private final Compartida recursoCompartido;

    public Autobus(int numero, Compartida recursoCompartido) {
        this.id = String.format("B-%04d", numero);
        this.destino = (numero % 2 == 0) ? "Madrid" : "Barcelona";
        this.recursoCompartido = recursoCompartido;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // Llegada a la parada del centro de la ciudad y espera de pasajeros
                recursoCompartido.llegadaParadaCentro();

                // Viaje hacia el aeropuerto
                recursoCompartido.marchaAeropuerto();

                // Bajar pasajeros en el aeropuerto
                recursoCompartido.bajarPasajeros();

                // Aquí podrías incluir la lógica para el regreso al centro, si fuera necesario
            }
        } catch (Exception e) {
            System.out.println(this.id + " ha sido interrumpido.");
        }
    }

    public String imprimirAutobus() {
        return "Autobús con ID: " + this.id + " creado, con destino: " + this.destino;
    }
    
}
