package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private Scene scene;
    private Parent root;
    private static MyViewModel viewModel;

    public static void init(Stage primary, MyViewModel vm) {
        stage = primary;
        viewModel = vm;
    }

    public static MyViewModel getViewModel() { return viewModel; }

    private void go(ActionEvent e, String fxml) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    Parent root       = loader.load();

    // אם הקונטרולר מממש IView – הזרק את ה-ViewModel
    Object ctrl = loader.getController();
    if (ctrl instanceof IView v) v.bindViewModel(viewModel);

    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.centerOnScreen();
    stage.show();
}

/* כניסות ציבוריות */
public void switchToStart(ActionEvent e)      throws Exception { go(e,"StartScreen.fxml"); }
public void switchToProperties(ActionEvent e) throws Exception { go(e,"Properties.fxml");  }
public void switchToGame(ActionEvent e)       throws Exception { go(e,"Game.fxml");        }


//    public void switchToStart(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void switchToProperties(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("Properties.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void switchToGame(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//    public void switchToVictory() throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("Victory.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
    // --- special overload: when we reach victory from code (אין ActionEvent) ---
    public void switchToVictory() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Victory.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
