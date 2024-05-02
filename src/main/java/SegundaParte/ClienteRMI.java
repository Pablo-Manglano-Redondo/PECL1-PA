package SegundaParte;

import java.rmi.Naming;
import javax.swing.JTextField;


public class ClienteRMI extends Thread{

    //private JTextField pasajerosAeropuerto;
    private JTextField hangar;
    private JTextField taller;
    private JTextField areaEstacionamiento;
    private JTextField areaRodaje;

    public ClienteRMI(/*JTextField pasajerosAeropuerto, */JTextField hangar, JTextField taller, JTextField areaEstacionamiento, JTextField areaRodaje) {
       // this.pasajerosAeropuerto = pasajerosAeropuerto;
        this.hangar = hangar;
        this.taller = taller;
        this.areaEstacionamiento = areaEstacionamiento;
        this.areaRodaje = areaRodaje;
    }
    
    public void run() {
        while (true) {
            try {
                AeropuertoRemoto aeroR = (AeropuertoRemoto) Naming.lookup("//127.0.0.1/OR");
                //pasajerosAeropuerto.setText(String.valueOf(aeroR.));
                hangar.setText(String.valueOf(aeroR.avionesEnHangar()));
                taller.setText(String.valueOf(aeroR.avionesEnTaller()));
                areaEstacionamiento.setText(String.valueOf(aeroR.avionesEnAreaEstacionamiento()));
                areaRodaje.setText(String.valueOf(aeroR.avionesEnAreaRodaje()));
            } catch (Exception ex){
            }
        }
    }
}
