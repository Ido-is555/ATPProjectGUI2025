package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;
    private static MyViewModel viewModel;

    public static void init(Stage primary, MyViewModel vm) {
        stage = primary;
        viewModel = vm;
    }

    public static MyViewModel getViewModel() { return viewModel; }

    public static void switchTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // אם הקונטרולר הוא IView – חבר את ה-ViewModel
            Object controller = loader.getController();
            if (controller instanceof IView v) v.bindViewModel(viewModel);

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
