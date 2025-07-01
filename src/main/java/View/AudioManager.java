package View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

// --- audio management ---
public class AudioManager {

    /* --- singleton --- */
    private static final AudioManager inst = new AudioManager();
    public  static AudioManager get() { return inst; }

    private static final String DIR = "/audio/";
    private MediaPlayer bgPlayer;

    private AudioManager(){}

    // --- loop the chosen track ---
    public void playBackground(String file){
        stopBackground();
        if (file == null) return;
        URL url = getClass().getResource(DIR + file + ".mp3");
        if (url == null) return;
        bgPlayer = new MediaPlayer(new Media(url.toExternalForm()));
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgPlayer.play();
    }
    public void stopBackground(){
        if (bgPlayer != null){ bgPlayer.stop(); bgPlayer = null; }
    }
}
