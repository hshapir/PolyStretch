/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

/**
 *
 * @author eblair
 */
public class Control {
    public long distance;
    public static int totalStepCount;
    
    public static void stepMotor(int numSteps){
        if(numSteps > 0){
            for(int i = 0; i < numSteps; i++){
                Control.totalStepCount++;
            }
        }
    }
    
    public static void returnToStart(){
        stepMotor(Control.totalStepCount * (-1));
        Control.totalStepCount = 0;
    }
    
    public static void setAsStart(){
        Control.totalStepCount = 0;
    }
}
