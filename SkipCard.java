public class SkipCard extends Card {
    /**
     * für Kommentare siehe in Klasse Card
     */
    public SkipCard() {
        super("SKIP");
    }
    
    public String getId() {
        return id;
    }
    

    public void doEf(Game pGame) {
        pGame.changeTurn(); //nächster Spieler ohne Ziehen
    }
}