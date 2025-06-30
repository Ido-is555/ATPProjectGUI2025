package View;

public enum Theme {

    /* --- princess theme --- */
    PRINCESS ("/View/Princess.css",
            "princessMusic",          /* background loop   */
            "giveUpPrincesses"),      /* give-up one-shot  */

    /* --- haunted house theme --- */
    WITCH    ("/View/HauntedHouse.css",
            "hauntedHouseMusic",
            "hauntedHouseMusic"),                    /* no special give-up sound */

    /* --- nemo theme --- */
    NEMO     ("/View/Nemo.css",
            "nemoMusic",
            "giveUpNemo");

    /* --- ctor & fields --- */
    private final String cssPath, bgMusic, giveUpSfx;
    Theme(String css, String bg, String giveUp){
        this.cssPath = css; this.bgMusic = bg; this.giveUpSfx = giveUp;
    }
    public String css ()      { return cssPath; }
    public String bg  ()      { return bgMusic; }
    public String sfxGiveUp() { return giveUpSfx; }
}