package View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartScreenController {

    @FXML private Button btnPrincess;
    @FXML private Button btnWitch;
    @FXML private Button btnNemo;

    private final SceneManager sceneManager = new SceneManager();

    // --- princess theme selected ---
    public void onPrincessTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.PRINCESS);
        SceneManager.setStyle("Princess.css");   // ---> css based on chosen theme
        sceneManager.switchToProperties(event);                 // --- open size-picker ---
    }

    // --- haunted-house theme selected ---
    public void onWitchTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.WITCH);
        SceneManager.setStyle("HauntedHouse.css"); // ---> css based on chosen theme
        sceneManager.switchToProperties(event);
    }

    // --- nemo theme selected ---
    public void onNemoTheme(ActionEvent event) throws Exception {
        AudioManager.get().stopBackground();
        SceneManager.applyTheme(Theme.NEMO);
        SceneManager.setStyle("Nemo.css"); // ---> css based on chosen theme
        sceneManager.switchToProperties(event);
    }

    @FXML
    public void initialize() {
        setupHoverSound(btnPrincess, "princess");
        setupHoverSound(btnWitch, "witch");
        setupHoverSound(btnNemo, "nemo");
    }

    // --- hover sound based on chosen theme ---
    private void setupHoverSound(Button btn, String soundName) {
        AudioManager.get().stopBackground();
        btn.setOnMouseEntered(e -> AudioManager.get().playBackground(soundName));
        AudioManager.get().stopBackground();
    }
}

