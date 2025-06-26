package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

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
            // הסר סלאש מוביל
            String cleanPath = fxmlPath.startsWith("/") ? fxmlPath.substring(1) : fxmlPath;

            // חפש יחסית לתיקיית src/main/java
            Path currentPath = Paths.get("src", "main", "java", cleanPath.split("/"));

            if (!Files.exists(currentPath)) {
                System.err.println("FXML NOT FOUND: " + currentPath.toAbsolutePath());
                return;
            }

            FXMLLoader loader = new FXMLLoader(currentPath.toUri().toURL());
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof IView v) v.bindViewModel(viewModel);

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void switchTo(String fxmlPath) {
//        try {
//            URL url = SceneManager.class.getResource(fxmlPath);
//            if (url == null) {
//                System.err.println("FXML NOT FOUND: " + fxmlPath);   //  <<< בדיקת דיבאגר
//                return;
//            }
//            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
//            Parent root = loader.load();
//
//            // אם הקונטרולר הוא IView – חבר את ה-ViewModel
//            Object controller = loader.getController();
//            if (controller instanceof IView v) v.bindViewModel(viewModel);
//
//            stage.setScene(new Scene(root));
//            stage.centerOnScreen();
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
