public class Game {

    private DCServer server;
    private String id;

    private PlayerList players;
    private CardList pile;

    public Game(String pId, DCServer pServer) {
        id = pId;
        server = pServer;
        players = new PlayerList();
        pile = new CardList();
    }

    /**
     * Fügt einen Spieler dem Spiel hinzu
     *
     * @param pPlayer der hinzuzufügende Spieler
     */
    public void addPlayer(Player pPlayer) {
        players.append(pPlayer);
    }

    /**
     * Entfernt einen Spieler aus dem Spiel
     *
     * @param pPlayer der zuentfernende Spieler
     */
    public void removePlayer(Player pPlayer) {
        players.toFirst();
        while (players.hasAccess()) {
            if (pPlayer == players.getContent()) {
                players.remove();
                return;
            }
            players.next();
        }
    }

    /**
     * Gibt eine Karte vom Kartenstapel zurück
     *
     * @return die oberste Karte
     */
    public Card popPile() {
        return pile.pull();
    }

    /**
     * Setzt den Spieler am Zug auf den nächsten
     */
    public void changeTurn() {
        players.getNext();
    }

    /**
     * Gibt den aktuellen Spieler am Zug zurück
     *
     * @return der Spieler am Zug
     */
    public Player getTurn() {
        return players.getTurn();
    }

    /**
     * Gibt die Spielerliste zurück
     *
     * @return die Spielerliste
     */
    public PlayerList getPlayers() {
        return players;
    }

    /**
     * Gibt den Kartenstapel zurück
     *
     * @return der Kartenstapel
     */
    public CardList getPile() {
        return pile;
    }

    /**
     * Gibt den Spielcode zurück
     *
     * @return der Spielcode
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt den Server zurück
     *
     * @return der Server
     */
    public DCServer getServer() {
        return server;
    }

}