package View;

public enum Theme {

    // --- princess theme ---
    PRINCESS ("/View/Princess.css", // ---> css path
            "princessMusic", // ---> background music
             "giveUpPrincesses"), // ---> give-up one-shot

    // --- haunted house theme ---
    WITCH    ("/View/HauntedHouse.css",
            "hauntedHouseMusic",
            "hauntedHouseMusic"),

    // --- nemo theme ---
    NEMO     ("/View/Nemo.css",
            "nemoMusic",
            "giveUpNemo");

    // --- css and sounds for each theme ---
    private final String cssPath, bgMusic, giveUpSfx;
    Theme(String css, String bg, String giveUp){
        this.cssPath = css; this.bgMusic = bg; this.giveUpSfx = giveUp;
    }
    public String css ()      { return cssPath; }
    public String bg  ()      { return bgMusic; }
    public String sfxGiveUp() { return giveUpSfx; }
}