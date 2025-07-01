package View;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.io.IOException;

public class VictoryController {
    private SceneManager sceneManager = new SceneManager();

    // --- swtich to start screen when pressing on new game button ---
    @FXML private void onNewGame(ActionEvent event) {
        try {
            sceneManager.switchToStart(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
