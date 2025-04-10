public class Player {

    private String ip;
    private int port;

    private String name;

    private List<Card> cardlist;
    private Game game;

    private boolean alive;

    public Player(String pIp, int pPort, String pName) {
        ip = pIp;
        port = pPort;
        name = pName;
    }

    /**
     * Setzt das aktuelle Spiel des Spielers und erstellt falls nötig eine neue Kartenliste
     *
     * @param pGame das Spiel
     */
    public void setGame(Game pGame) {
        if (game != pGame) {
            cardlist = new List<>();
        }
        game = pGame;
    }

    /**
     * Setzt den Lebensstatus des Spielers auf den gegebenen Wert true oder false
     *
     *  @param pStatus ob der Spieler lebt
     */
    public void setAlive(boolean pStatus) {
        alive = pStatus;
    }

    /**
     * Fügt eine Karte auf die Hand des Spielers hinzu
     *
     * @param pCard die hinzuzufügende Karte
     */
    public void addCard(Card pCard) {
        cardlist.append(pCard);
    }

    /**
     * Entfernt eine Karte von der Hand des Spielers
     *
     *  @param pCard die zu entfernende Karte
     */
    public void removeCard(Card pCard) {
        cardlist.toFirst();
        while (cardlist.hasAccess()) {
            if (cardlist.getContent() == pCard) {
                cardlist.remove();
                return;
            }
            cardlist.next();
        }
    }

    /**
     * Gibt die IP des Spielers zurück
     *
     * @return die IP
     */
    public String getIP() {
        return ip;
    }

    /**
     * Gibt den Port des Spielers zurück
     *
     * @return der Port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gibt den Namen des Spielers zurück
     *
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Hand der Karten des Spielers zurück
     *
     * @return die Kartenhand
     */
    public List<Card> getCards() {
        return cardlist;
    }

    /**
     * Gibt das aktuelle Spiel des Spielers zurück
     *
     * @return das aktuelle Spiel
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gibt zurück, ob der Spieler noch am Leben ist
     *
     * @return ob der Spieler lebt
     */
    public boolean isAlive() {
        return alive;
    }

}