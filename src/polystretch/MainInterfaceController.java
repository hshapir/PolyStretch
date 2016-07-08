/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

/**
 *
 * @author Harrison Shapiro
 */
public class MainInterfaceController implements Initializable {
    //FXML variables go under here
    @FXML
    private TextField strainOutputField;
    
    @FXML
    private TextField numStepsField;
    
    @FXML
    private Button oneStepButton;
    
    @FXML
    private Button customStepsButton;
    
    @FXML
    private Button motorResetButton;
    
    @FXML
    private Button reverseStepButton;
    
    @FXML
    private Button recalibrateButton;
    
    @FXML
    private MenuItem closeButton;
    
    @FXML
    private MenuItem aboutButton;
    
    //Other variables go under here
    
    
    @FXML
    public void quit(ActionEvent event){
        Platform.exit();
        
    }
    
    public void moveOneStepForward(){
        Control.stepMotor(1);
    }
    
    public void moveCustomStepsForward(){
        String numSteps = numStepsField.getText();
        Integer steps = null;
        try{
            steps = Integer.parseInt(numSteps);
        }catch(Exception e){
            sendInvalidStepsAlert();
            return;
        }
        if(steps > 0){
            Control.stepMotor(steps);
        } else{
            sendInvalidStepsAlert();
        }
    }
    
    private void sendInvalidStepsAlert(){
        Alert invalidStepsAlert = new Alert(Alert.AlertType.INFORMATION);
        invalidStepsAlert.setTitle("Invalid Number of Steps");
        invalidStepsAlert.setHeaderText(null);
        invalidStepsAlert.setContentText("Please enter a valid number of steps. Only whole positive integers will be accepted");
        invalidStepsAlert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
