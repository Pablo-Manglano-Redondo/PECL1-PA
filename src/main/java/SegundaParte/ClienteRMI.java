package SegundaParte;

import PrimeraParte.Compartida;
import java.rmi.Naming;
import javax.swing.JButton;
import javax.swing.JTextField;


public class ClienteRMI extends Thread{

    private JTextField txtExtColonia;
    private JTextField txtIntColonia;
    private JTextField txtInstruccion;
    private JTextField txtRepInvasion;
    private JTextField txtComedor;
    private JTextField txtRefugio;
    private JButton botonAmenaza;
    private boolean amenazar = false;
    
    public ClienteRMI(JTextField txtExtColonia, JTextField txtIntColonia, JTextField txtInstruccion, 
            JTextField txtRepInvasion, JTextField txtComedor, JTextField txtRefugio, JButton Amenaza) {
        this.txtExtColonia = txtExtColonia;
        this.txtIntColonia = txtIntColonia;
        this.txtInstruccion = txtInstruccion;
        this.txtRepInvasion = txtRepInvasion;
        this.txtComedor = txtComedor;
        this.txtRefugio = txtRefugio;
        this.botonAmenaza = Amenaza;
    }
    
    
    public void run() {
        while (true){
            try{
                HormigueroRemoto hr = (HormigueroRemoto) Naming.lookup("//127.0.0.1/OR");

                txtExtColonia.setText(String.valueOf(hr.obrerasFuera()));
                txtIntColonia.setText(String.valueOf(hr.obrerasDentro()));
                txtInstruccion.setText(String.valueOf(hr.soldadoInstruccion()));
                txtRepInvasion.setText(String.valueOf(hr.soldadoInvasion()));
                txtComedor.setText(String.valueOf(hr.criasComedor()));
                txtRefugio.setText(String.valueOf(hr.criasRefugio()));

                while (hr.hacerAmenaza()){
                    botonAmenaza.setVisible(false);
                    txtExtColonia.setText(String.valueOf(hr.obrerasFuera()));
                    txtIntColonia.setText(String.valueOf(hr.obrerasDentro()));
                    txtInstruccion.setText(String.valueOf(hr.soldadoInstruccion()));
                    txtRepInvasion.setText(String.valueOf(hr.soldadoInvasion()));
                    txtComedor.setText(String.valueOf(hr.criasComedor()));
                    txtRefugio.setText(String.valueOf(hr.criasRefugio()));
                }  if (amenazar){
                    hr.causarAmenaza();
                    botonAmenaza.setVisible(false);
                    amenazar = false;
                }
                    botonAmenaza.setVisible(true);

            } catch(Exception e){
                
            }
            
        }
    }

    public void setAmenazar(boolean amenazar) {
        this.amenazar = amenazar;
    }
    
}
