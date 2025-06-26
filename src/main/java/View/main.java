package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage stage) {
        // יצירת Model & ViewModel יחידים לאפליקציה
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);

        // SceneManager מחזיק את ה-Stage וה-ViewModel
        SceneManager.init(stage, viewModel);
        SceneManager.switchTo("/View/StartScreen.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}


