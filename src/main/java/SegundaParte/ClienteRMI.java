package SegundaParte;

import java.rmi.Naming;
import javax.swing.JTextField;

public class ClienteRMI extends Thread{
    
    private JTextField pasajerosAeropuerto;
    private JTextField hangar;
    private JTextField taller;
    private JTextField areaEstacionamiento;
    private JTextField areaRodaje;
    private JTextField aerovia;
    private String l;
    public ClienteRMI(JTextField pasajerosAeropuerto, JTextField hangar, JTextField taller, 
            JTextField areaEstacionamiento, JTextField areaRodaje, JTextField aerovia, String l) {
        this.pasajerosAeropuerto = pasajerosAeropuerto;
        this.hangar = hangar;
        this.taller = taller;
        this.areaEstacionamiento = areaEstacionamiento;
        this.areaRodaje = areaRodaje;
        this.aerovia = aerovia;
        this.l = l;
    }
    
    public void setEstadoPista(int pistaId, boolean estado) {
        System.out.println("ClienteRMI");
        try {
            AeropuertoRemoto aeroR = (AeropuertoRemoto) Naming.lookup("//127.0.0.1/OR" + l);
            aeroR.setEstadoPista(pistaId, estado);
        } catch (Exception ex) {
            ex.printStackTrace();  // Asegúrate de manejar correctamente las excepciones
        }
    }
    
    public void run() {
        while (true) {
            try {
                AeropuertoRemoto aeroR = (AeropuertoRemoto) Naming.lookup("//127.0.0.1/OR" + l);
                pasajerosAeropuerto.setText(String.valueOf(aeroR.pasajerosAeropuerto()));
                hangar.setText(String.valueOf(aeroR.avionesEnHangar()));
                taller.setText(String.valueOf(aeroR.avionesEnTaller()));
                areaEstacionamiento.setText(String.valueOf(aeroR.avionesEnAreaEstacionamiento()));
                areaRodaje.setText(String.valueOf(aeroR.avionesEnAreaRodaje()));
                aerovia.setText(aeroR.aerovia());
            } catch (Exception ex){
            }
        }
    }
}
