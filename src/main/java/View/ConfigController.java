package View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ViewModel.ConfigLoader;
import java.util.Properties;
public class ConfigController {
    @FXML private VBox container;
    @FXML
    public void initialize() {
        Properties props = ConfigLoader.getAll();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            Label label = new Label(key + ": " + value);
            label.setStyle("-fx-font-size: 16px;");
            container.getChildren().add(label);
        }
    }
    }