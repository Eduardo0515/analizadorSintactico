/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsintactico;

import analizadorlexico.principal.AutomataPrincipal;
import analizadorsintactico.analisisnorecursivo.AnalisisNoRecursivo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import leerarchivo.LeerArchivo;

/**
 *
 * @author Hugo Ruiz
 */
public class FXMLDocumentController implements Initializable {

    String[][] tabla;
    AutomataPrincipal automataPrincipal;
    AnalisisNoRecursivo analisis;

    ArrayList lexemas;
    ArrayList tokens;
    
    @FXML
    private TextArea entradaTxt;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        automataPrincipal.analizar(entradaTxt.getText());
        lexemas = automataPrincipal.getElementos();
        tokens = automataPrincipal.getSignificados();
        System.out.println(lexemas);
        System.out.println(tokens);
        try {
            leerTabla();
            analisis = new AnalisisNoRecursivo(lexemas, tabla);
            analisis.metodoPredictivoNoRecursivo();
            System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        automataPrincipal = new AutomataPrincipal();
    }

    
    public void leerTabla() throws IOException{
        LeerArchivo leerArchivo = new LeerArchivo();
        leerArchivo.leerExcel();
        tabla = leerArchivo.getTabla();
        
        for(int i=0; i<39; i++){
            for(int j=0; j<28; j++){
                System.out.print(tabla[i][j]+" ");
            }
            System.out.println("");
        }
    }

}
