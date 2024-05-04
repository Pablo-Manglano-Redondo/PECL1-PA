package PrimeraParte;

import java.util.ArrayList;
import javax.swing.JTextField;

public class ListaVehiculos {
    private JTextField textField;
    private ArrayList<String> lista;
    
    
    public ListaVehiculos(JTextField textField){
        this.textField = textField;
        lista = new ArrayList<>();
    }
    
    public synchronized void añadir(String s){
        lista.add(s);
        String text = textField.getText();
        textField.setText(text + s + ", ");
    }
    
    public synchronized void quitar(String s){
        lista.remove(s);
        String text = "";
        for(int i = 0; i < lista.size(); i++){
            text+= lista.get(i);
            text+=", ";
            
        }
        textField.setText(text);
    }
    
    public synchronized ArrayList<String> getLista(){
        return lista;
    }
    
    public String obtenerLista() {
        String contenedor="";
        for(int i=0; i<lista.size();i++){
            contenedor=contenedor+lista.get(i)+",";
                    
        }
        return contenedor;
    }
}
