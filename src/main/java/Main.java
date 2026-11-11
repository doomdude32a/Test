import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Starts a small window with one button that switches an LED on and off.
 * On a laptop (no Pi) the button still works, only the LED is missing.
 */
public final class Main {

    private Main() {
    }

    /** @param args command line arguments */
    public static void main(String[] args) {
        Application.launch(Window.class, args);
    }

    /**
     * The JavaFX window.
     */
    public static final class Window extends Application {

        private static final int LED_PIN = 22;

        private Context pi4j;
        private DigitalOutput led;
        private boolean ledOn;

        @Override
        public void start(Stage stage) {
            connectLed();
            Button button = new Button("LED on");
            button.setOnAction(event -> {
                ledOn = !ledOn;
                if (led != null) {
                    led.state(ledOn ? DigitalState.HIGH : DigitalState.LOW);
                }
                button.setText(ledOn ? "LED off" : "LED on");
            });
            stage.setTitle("Drive Buy Tiger");
            stage.setScene(new Scene(button, 300, 200));
            stage.show();
        }

        @Override
        public void stop() {
            if (pi4j != null) {
                pi4j.shutdown();
            }
        }

        private void connectLed() {
            try {
                pi4j = Pi4J.newAutoContext();
                led = pi4j.dout().create(LED_PIN);
            } catch (RuntimeException | LinkageError e) {
                System.out.println("No LED found (not on a Pi?): " + e);
            }
        }
    }
}
