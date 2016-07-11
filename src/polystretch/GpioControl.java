/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polystretch;

/**
 *
 * @author Harrison Shapiro
 */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.util.Duration;

/*
 *
 * @author Jens Deters
 */
public class GpioControl {

    private GpioController gpio;
    
    
    private GpioPinDigitalMultipurpose pin18;
 
    private GpioPinDigitalMultipurpose pin22;
    private GpioPinDigitalMultipurpose pin23;
    private GpioPinDigitalMultipurpose pin24;
    private GpioPinDigitalMultipurpose pin04;
    private GpioPinDigitalMultipurpose pin17;
    private ObjectProperty<PinMode> gpio18ModeProperty;
    private ObjectProperty<PinMode> gpio22ModeProperty;
    private ObjectProperty<PinMode> gpio23ModeProperty;
    private ObjectProperty<PinMode> gpio24ModeProperty;
    private ObjectProperty<PinMode> gpio04ModeProperty;
    private ObjectProperty<PinMode> gpio17ModeProperty;
    private BooleanProperty gpio18StateProperty;
    private BooleanProperty gpio22StateProperty;
    private BooleanProperty gpio23StateProperty;
    private BooleanProperty gpio24StateProperty;
    private BooleanProperty gpio04StateProperty;
    private BooleanProperty gpio17StateProperty;
    private BooleanProperty connectedProperty;
    private Timeline testTimeline;
    private GpioPinDigitalMultipurpose[] pins;
    private BooleanProperty[] stateProperties;
    private ObjectProperty<PinMode>[] modeProperties;
    private final static Logger LOGGER = Logger.getLogger(GpioControl.class.getName());

    public GpioControl() {
        connectedProperty = new SimpleBooleanProperty(Boolean.FALSE);

        gpio18StateProperty = new SimpleBooleanProperty(Boolean.FALSE);
        gpio18ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);
        
        gpio22StateProperty = new SimpleBooleanProperty(Boolean.FALSE);
        gpio23StateProperty = new SimpleBooleanProperty(Boolean.FALSE);
        gpio24StateProperty = new SimpleBooleanProperty(Boolean.FALSE);
        gpio04StateProperty = new SimpleBooleanProperty(Boolean.FALSE);
        gpio17StateProperty = new SimpleBooleanProperty(Boolean.FALSE);

        stateProperties = new BooleanProperty[]{gpio18StateProperty,
            gpio22StateProperty,
            gpio23StateProperty,
            gpio24StateProperty,
            gpio04StateProperty,
            gpio17StateProperty,
        };

        gpio22ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);
        gpio23ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);
        gpio24ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);
        gpio04ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);
        gpio17ModeProperty = new SimpleObjectProperty<>(PinMode.DIGITAL_OUTPUT);

        modeProperties = new ObjectProperty[]{
            gpio18ModeProperty,
            gpio22ModeProperty,
            gpio23ModeProperty,
            gpio24ModeProperty,
            gpio04ModeProperty,
            gpio17ModeProperty,
        };

    }

    
    private ChangeListener<Boolean> createPinStatePropertyListener(final GpioPinDigitalMultipurpose pin) {
        return (ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
            LOGGER.log(Level.INFO, "pinPropertyChanged: {0} {1}", new Object[]{pin.getName(), newValue});
            if (pin.getMode() != PinMode.DIGITAL_INPUT) {
                if (newValue) {
                    pin.high();
                } else {
                    pin.low();
                }
            }
        };
    }

    private void addGpioInputListener(final GpioPinDigitalMultipurpose pin, final BooleanProperty gpioStateProperty) {
        pin.addListener((GpioPinListenerDigital) (final GpioPinDigitalStateChangeEvent event) -> {
            LOGGER.log(Level.INFO, "pinstateChanged: {0} {1}", new Object[]{pin.getName(), event.getState()});
            Platform.runLater(() -> {
                gpioStateProperty.set(event.getState().
                        isHigh());
            });
        });
    }

    /*
     * -------------------------- ACTIONS -------------------------- 
     */
    public void connect() {
        LOGGER.log(Level.INFO, "connect...");

        gpio = GpioFactory.getInstance();
        
        pin18 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_00, gpio18ModeProperty.
                get(), PinPullResistance.PULL_DOWN);
        
        pin22 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, gpio22ModeProperty.
                get(), PinPullResistance.PULL_DOWN);

        pin23 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_02, gpio23ModeProperty.
                get(), PinPullResistance.PULL_DOWN);

        pin24 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_03, gpio24ModeProperty.
                get(), PinPullResistance.PULL_DOWN);

        pin04 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_04, gpio04ModeProperty.
                get(), PinPullResistance.PULL_DOWN);

        pin17 = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_05, gpio17ModeProperty.
                get(), PinPullResistance.PULL_DOWN);

        pins = new GpioPinDigitalMultipurpose[]{
            pin18, pin22, pin23, pin24, pin04, pin17
        };
        gpio.setShutdownOptions(true, PinState.LOW, pins);

        
        gpio18StateProperty.addListener(createPinStatePropertyListener(pin18));
        addGpioInputListener(pin18, gpio18StateProperty);

        
        addGpioInputListener(pin22, gpio22StateProperty);
        addGpioInputListener(pin23, gpio23StateProperty);
        addGpioInputListener(pin24, gpio24StateProperty);
        addGpioInputListener(pin04, gpio04StateProperty);
        addGpioInputListener(pin17, gpio17StateProperty);

        gpio22StateProperty.addListener(createPinStatePropertyListener(pin22));
        gpio23StateProperty.addListener(createPinStatePropertyListener(pin23));
        gpio24StateProperty.addListener(createPinStatePropertyListener(pin24));
        gpio04StateProperty.addListener(createPinStatePropertyListener(pin04));
        gpio17StateProperty.addListener(createPinStatePropertyListener(pin17));

        reset();
        setConnectedPropertyValue(Boolean.TRUE);
        LOGGER.log(Level.INFO, "connected.");

    }

    public void setOnAllPins() {
        LOGGER.log(Level.INFO, "setOnAllPins()");
        for (int i = 0; i <= 5; i++) {
            stateProperties[i].setValue(Boolean.TRUE);
        }
    }

    public void setOffAllPins() {
        LOGGER.log(Level.INFO, "setOffAllPins()");
        for (int i = 0; i <= 5; i++) {
            stateProperties[i].setValue(Boolean.FALSE);
        }
    }

    public void disconnect() {
        LOGGER.log(Level.INFO, "disconnect()");
        if (gpio != null) {
            gpio.shutdown();
        }
        setConnectedPropertyValue(Boolean.FALSE);

    }

    public void resetIOModes() {
        LOGGER.log(Level.INFO, "resetIOModes()");
        for (int i = 0; i <= 5; i++) {
            setGpioMode(i, PinMode.DIGITAL_OUTPUT);
        }
    }

    public void reset() {
        LOGGER.log(Level.INFO, "reset()");
        if (testTimeline != null) {
            testTimeline.stop();
        }
        setOffAllPins();
        resetIOModes();

    }

    public void connectTest() {
        LOGGER.log(Level.INFO, "connectTest()");
        if (testTimeline != null) {
            testTimeline.stop();
        }
        reset();

        testTimeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            setOnAllPins();
        }), new KeyFrame(Duration.seconds(0.1), (ActionEvent event) -> {
            setOffAllPins();
        }));

        testTimeline.play();
    }

    public void test(double millis) {
        LOGGER.log(Level.INFO, "test()");
        reset();
        testTimeline = new Timeline(new KeyFrame(Duration.millis(millis), (ActionEvent event) -> {
            gpio18StateProperty.setValue(Boolean.TRUE);
        }), new KeyFrame(Duration.millis(millis * 2), (ActionEvent event) -> {
            gpio22StateProperty.setValue(Boolean.TRUE);
        }), new KeyFrame(Duration.millis(millis * 3), (ActionEvent event) -> {
            gpio23StateProperty.setValue(Boolean.TRUE);
        }), new KeyFrame(Duration.millis(millis * 4), (ActionEvent event) -> {
            gpio24StateProperty.setValue(Boolean.TRUE);
        }), new KeyFrame(Duration.millis(millis * 5), (ActionEvent event) -> {
            gpio04StateProperty.setValue(Boolean.TRUE);
        }), new KeyFrame(Duration.millis(millis * 6), (ActionEvent event) -> {
            gpio17StateProperty.setValue(Boolean.TRUE);
        }));
        testTimeline.play();
    }

    /*
     * -------------------------- PROPERTY METHODS -------------------------- 
     */
    public void setGpioStateValue(int pinNumber, Boolean state) {
        stateProperties[pinNumber].setValue(state);
    }

    public void setGpioMode(int pinNumber, PinMode mode) {
        modeProperties[pinNumber].setValue(mode);
        if (isConnected()) {
            pins[pinNumber].setMode(mode);
        }
    }

    public void setConnectedPropertyValue(Boolean connected) {
        this.connectedProperty.setValue(connected);
    }

    public boolean isConnected() {
        return connectedProperty.get();
    }

    public BooleanProperty connectedProperty() {
        return connectedProperty;
    }

    public BooleanProperty gpio18StateProperty() {
        return gpio18StateProperty;
    }

    public BooleanProperty gpio22StateProperty() {
        return gpio22StateProperty;
    }

    public BooleanProperty gpio23StateProperty() {
        return gpio23StateProperty;
    }

    public BooleanProperty gpio24StateProperty() {
        return gpio24StateProperty;
    }

    public BooleanProperty gpio04StateProperty() {
        return gpio04StateProperty;
    }

    public BooleanProperty gpio17StateProperty() {
        return gpio17StateProperty;
    }

    public ObjectProperty<PinMode> gpio18ModeProperty() {
        return gpio18ModeProperty;
    }

    public ObjectProperty<PinMode> gpio22ModeProperty() {
        return gpio22ModeProperty;
    }

    public ObjectProperty<PinMode> gpio23ModeProperty() {
        return gpio23ModeProperty;
    }

    public ObjectProperty<PinMode> gpio24ModeProperty() {
        return gpio24ModeProperty;
    }

    public ObjectProperty<PinMode> gpio04ModeProperty() {
        return gpio04ModeProperty;
    }

    public ObjectProperty<PinMode> gpio17ModeProperty() {
        return gpio17ModeProperty;
    }
}