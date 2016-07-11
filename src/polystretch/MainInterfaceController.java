/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;

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
    
    public Control controller;
    
    //Other variables go under here
    
    
    @FXML
    public void quit(ActionEvent event){
        Platform.exit();
        
    }
    
    public void moveOneStepForward(){
        controller.stepMotor(1);
    }
    
    public void moveOneStepBackward(){
        controller.stepMotor(-1);
    }
    
    public void resetStepCount(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are You Sure?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to reset the step count? This will make automatic resets impossible. See the about tab in the help menu for more information on calibration.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            controller.setAsStart();
        }  
    }
    
    
    public void backToStartingPosition(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are You Sure?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return to the starting position? You will not be able to continue to stretch this polymer sample and you will lose your microscope view.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            controller.returnToStart();
        }
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
            controller.stepMotor(steps);
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
        controller = new Control();
    }    
    
}
