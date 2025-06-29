package View;

import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;


public class StartScreenController {

    // --- single SceneManager instance (keeps the stage & ViewModel) ---
    private final SceneManager sceneManager = new SceneManager();

    // --- princess theme selected ---
    public void onPrincessTheme(ActionEvent event) throws Exception {
        SceneManager.setStyle("Princess.css");   // --- save chosen theme ---
        sceneManager.switchToProperties(event);                 // --- open size-picker ---
    }

    // --- haunted-house theme selected ---
    public void onWitchTheme(ActionEvent event) throws Exception {
        SceneManager.setStyle("HauntedHouse.css");
        sceneManager.switchToProperties(event);
    }

    // --- nemo theme selected ---
    public void onNemoTheme(ActionEvent event) throws Exception {
        SceneManager.setStyle("Nemo.css");
        sceneManager.switchToProperties(event);
    }
}

