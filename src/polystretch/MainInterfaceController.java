/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Harrison Shapiro
 */
public class MainInterfaceController implements Initializable {
    //FXML variables go under here
    @FXML
    private Label label;
    
    //Other variables go under here
    

    @FXML
    public void quit(ActionEvent event){
        System.exit(0);   
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
