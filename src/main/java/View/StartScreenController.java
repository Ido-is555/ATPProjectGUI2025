package View;

import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;


public class StartScreenController {

    @FXML private Button btnPrincess;
    @FXML private Button btnWitch;
    @FXML private Button btnNemo;

    // --- single SceneManager instance (keeps the stage & ViewModel) ---
    private final SceneManager sceneManager = new SceneManager();

    // --- princess theme selected ---
    public void onPrincessTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.PRINCESS);
        SceneManager.setStyle("Princess.css");   // --- save chosen theme ---
        sceneManager.switchToProperties(event);                 // --- open size-picker ---
    }

    // --- haunted-house theme selected ---
    public void onWitchTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.WITCH);
        SceneManager.setStyle("HauntedHouse.css");
        sceneManager.switchToProperties(event);
    }

    // --- nemo theme selected ---
    public void onNemoTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.NEMO);
        SceneManager.setStyle("Nemo.css");
        sceneManager.switchToProperties(event);
    }

    @FXML
    public void initialize() {
        setupHoverSound(btnPrincess, "princess");
        setupHoverSound(btnWitch, "witch");
        setupHoverSound(btnNemo, "nemo");
    }

    /** מאזין להצלבת hover שמפעיל סאונד לפי שם הכפתור */
    private void setupHoverSound(Button btn, String soundName) {
        AudioManager.get().stopBackground();
        btn.setOnMouseEntered(e -> AudioManager.get().playBackground(soundName));
        AudioManager.get().stopBackground();
    }
}

