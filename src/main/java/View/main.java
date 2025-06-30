package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage stage) {

        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        SceneManager.init(stage, viewModel);
        SceneManager.setStage(stage);

        try{
            Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


