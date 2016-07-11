/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
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
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
/**
 *
 * @author eblair
 */
public class Control {
    private final static Logger LOGGER = Logger.getLogger(Control.class.getName());
    private GpioController gpio;
    private GpioStepperMotorComponent motor;
    private byte[] stepSequence;
    private BooleanProperty connectedProperty;
    
    public long distance;
    public int totalStepCount;
    
    
    public Control(){
        totalStepCount = 0;
        connectedProperty = new SimpleBooleanProperty(Boolean.FALSE);
        stepSequence = new byte[4];
        stepSequence[0] = (byte) 0b1010;
        stepSequence[1] = (byte) 0b0110;
        stepSequence[2] = (byte) 0b0101;
        stepSequence[3] = (byte) 0b1001;
    }
    
    
    public void setAsStart(){
        totalStepCount = 0;
    }
    
    public void returnToStart(){
        stepMotor(totalStepCount * (-1));
        totalStepCount = 0;
    }
    public void connect(){
        LOGGER.info("connect...");
        gpio = GpioFactory.getInstance();
    
        /*
        Pins 18 and 22 are used to enable the stepper motor - they are always kept on high when the motor is to be used.
        Pins 23, 24, 04, and 17 are used to control the 
        
        */
        
        final GpioPinDigitalOutput[] pins = {
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, PinState.HIGH),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.HIGH),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, PinState.LOW)
        };
        //ensures that the motor is shut down when the program terminates
        gpio.setShutdownOptions(true, PinState.LOW, PinPullResistance.PULL_DOWN, pins);
        
        //creates motor
        motor = new GpioStepperMotorComponent(pins);
        motor.setStepSequence(stepSequence);
        motor.setStepsPerRevolution(200);
        motor.setStepInterval(1);
        connectedProperty.setValue(Boolean.TRUE);
    }
    
    public void disconnect(){
        LOGGER.info("disconnect.");
        if(gpio != null){
            gpio.shutdown();
        }
        connectedProperty.setValue(Boolean.FALSE);
    }
    
    public boolean isConnected(){
        return connectedProperty.getValue();
    }
    
    public void stepMotor(int steps){
        motor.step(-steps);
        totalStepCount += steps;
        
    }
   public GpioStepperMotorComponent getMotor(){
       return motor;      
   }
}
