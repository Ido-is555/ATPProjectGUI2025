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
    private static String styleChose;
    /* --- current theme --- */
    private static Theme currentTheme;

    public static void init(Stage primary, MyViewModel vm) {
        stage = primary;
        viewModel = vm;
    }
    public static void applyTheme(Theme t){
        currentTheme = t;
        AudioManager.get().playBackground(t.bg());   // << play proper loop
    }
    public static void setStyle(String style){
        styleChose = style;
    }

    public static MyViewModel getViewModel() { return viewModel; }

    private void go(ActionEvent e, String fxml, String style) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    Parent root       = loader.load();

    // אם הקונטרולר מממש IView – הזרק את ה-ViewModel
    Object ctrl = loader.getController();
    if (ctrl instanceof IView v) v.bindViewModel(viewModel);

    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    scene = stage.getScene();
    scene.getStylesheets().add(getClass().getResource(style).toExternalForm());
    stage.centerOnScreen();
    stage.show();
}

public Theme getCurrentTheme(){ return currentTheme; }

/* כניסות ציבוריות */
public void switchToStart(ActionEvent e)      throws Exception { AudioManager.get().stopBackground(); go(e,"StartScreen.fxml", "style.css"); }
public void switchToProperties(ActionEvent e) throws Exception { go(e,"Properties.fxml", styleChose);  }
public void switchToGame(ActionEvent e)       throws Exception { go(e,"MyView.fxml", styleChose);        }


    // --- special overload: when we reach victory from code (אין ActionEvent) ---
    public void switchToVictory(String style) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Victory.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource(style).toExternalForm());
        stage.centerOnScreen();
        stage.show();
    }

    public static void setStage(Stage stage) {
        stage = stage;
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setMinWidth(800);   // אפשרי: גבול תחתון
        stage.setMinHeight(600);
    }
}