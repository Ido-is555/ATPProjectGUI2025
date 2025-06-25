package View;

import javafx.fxml.FXML;

public class VictoryController {

    @FXML private void onNewGame() {
        SceneManager.switchTo("/fxml/StartScreen.fxml");
    }
}
