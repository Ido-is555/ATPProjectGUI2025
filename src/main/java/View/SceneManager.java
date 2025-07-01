package View;
import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class SceneManager {

    private static Stage stage;
    private Scene scene;
    private static MyViewModel viewModel;
    private static String styleChose;
    private static Theme currentTheme; // --->  selected game theme

    public static void init(Stage primary, MyViewModel vm) {
        stage = primary;
        viewModel = vm;
    }
    public static void applyTheme(Theme t){
        currentTheme = t;
        AudioManager.get().playBackground(t.bg());
    }

    // --- theme choosing ---
    public static void setStyle(String style){
        styleChose = style;
    }

    public static MyViewModel getViewModel() { return viewModel; }

    // --- scene switcher ---
    private void go(ActionEvent e, String fxml, String style) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    Parent root = loader.load();

    Object ctrl = loader.getController();
    if (ctrl instanceof IView v) v.bindViewModel(viewModel);

    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    scene = stage.getScene();
    scene.getStylesheets().add(getClass().getResource(style).toExternalForm()); // ---> same whole theme for whole game
    stage.centerOnScreen();
    stage.show();
}

public Theme getCurrentTheme(){ return currentTheme; }

// --- scene switcher wrappers - sends the next fxml with its theme style ---
public void switchToStart(ActionEvent e)      throws Exception { AudioManager.get().stopBackground(); go(e,"StartScreen.fxml", "style.css"); }
public void switchToProperties(ActionEvent e) throws Exception { go(e,"Properties.fxml", styleChose);  }
public void switchToGame(ActionEvent e)       throws Exception { go(e,"MyView.fxml", styleChose);}
public void switchToVictory(String style) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("Victory.fxml"));
    scene = new Scene(root);
    stage.setScene(scene);
    scene.getStylesheets().add(getClass().getResource(style).toExternalForm());
    stage.centerOnScreen();
    stage.show();
}

public static void setStage(Stage stage) {
    stage.setWidth(1200);
    stage.setHeight(800);
    stage.setMinWidth(800);   // אפשרי: גבול תחתון
    stage.setMinHeight(600);
}
}