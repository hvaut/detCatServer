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
        // send protocol
        pGame.getPlayers().toFirst();
        while (pGame.getPlayers().hasAccess()) {
            Player current = pGame.getPlayers().getContent();
            pGame.getServer().send(current.getIp(), current.getPort(), "TURN " + pGame.getTurn().getName());
            pGame.getPlayers().next();
        }
    }
}