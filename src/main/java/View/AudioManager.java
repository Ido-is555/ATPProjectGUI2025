package View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/* ------------------------------------------------------------------
   --- simple helper for looping bg-music & one-shot sound effects ---
   ------------------------------------------------------------------ */
public class AudioManager {

    /* --- singleton --- */
    private static final AudioManager inst = new AudioManager();
    public  static AudioManager get() { return inst; }

    private static final String DIR = "/audio/";   /* resources/audio/ */
    private MediaPlayer bgPlayer;                  /* current loop     */

    private AudioManager(){}                       /* no public ctor   */

    /* --- loop chosen track indefinitely --- */
    public void playBackground(String file){
        stopBackground();
        if (file == null) return;
        URL url = getClass().getResource(DIR + file + ".mp3");
        if (url == null) return;                   /* file missing */
        bgPlayer = new MediaPlayer(new Media(url.toExternalForm()));
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgPlayer.play();
    }
    public void stopBackground(){
        if (bgPlayer != null){ bgPlayer.stop(); bgPlayer = null; }
    }

    /* --- fire-and-forget one-shot --- */
    public void playOnce(String file){
        if (file == null) return;
        URL url = getClass().getResource(DIR + file + ".mp3");
        if (url == null) return;
        new MediaPlayer(new Media(url.toExternalForm())).play();
    }
}
