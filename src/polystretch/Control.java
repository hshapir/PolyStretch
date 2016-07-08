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
/**
 *
 * @author eblair
 */
public class Control {
    public long distance;
    public static int totalStepCount;
    
    private static GpioStepperMotorComponent motor = null;
    
    
    public static void setAsStart(){
        Control.totalStepCount = 0;
    }
    
    public static void returnToStart(){
        Control.stepMotor(Control.totalStepCount * (-1));
        Control.totalStepCount = 0;
    }
    public static void setup(){
         final GpioController gpio = GpioFactory.getInstance();
    
        //NEED TO DO : provision pins for output, and ensure in LOW state
        final GpioPinDigitalOutput[] pins = {
            
        
        };
        //ensures that the motor is shut down when the program terminates
        gpio.setShutdownOptions(true, PinState.LOW, pins);
        
        //creates motor
        GpioStepperMotorComponent m = new GpioStepperMotorComponent(pins);
        motor = m;
        
        byte[] single_step_sequence = new byte[4];
        single_step_sequence[0] = (byte) 0b0001;
        single_step_sequence[1] = (byte) 0b0010;
        single_step_sequence[2] = (byte) 0b0100;
        single_step_sequence[3] = (byte) 0b1000;
        
        motor.setStepSequence(single_step_sequence);
        
        motor.setStepInterval(10);
  
    }
    
    public static void stepMotor(int steps){
        motor.step(steps);
        
    }
   public GpioStepperMotorComponent getMotor(){
       return motor;      
   }
}
