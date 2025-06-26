package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartScreenController {

    @FXML private Button btnStart;

    @FXML private void onStartClicked() {
        SceneManager.switchTo("/View/PropertiesScreen.fxml");
    }
}
