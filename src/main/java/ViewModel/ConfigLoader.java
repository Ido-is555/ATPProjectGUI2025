package ViewModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties props = new Properties();

        // --- fetch project properties from confic.properties ---
        static {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- getters ---
    public static String get(String key) {
        return props.getProperty(key);
    }

    public static Properties getAll() {
        return props;
    }
}