package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class StartScreenController {

    private SceneManager sceneManager = new SceneManager();
    @FXML private Button btnStart;

    @FXML private void onStartClicked(ActionEvent event) {
        try {
            sceneManager.switchToProperties(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
