public abstract class Card {

    private String id;

    /**
     * Der Allgemeine Konstruktur für eine Karte
     *
     * @param id die Id der Karte für Netzwerkübertragungen
     */
    public Card(String id) {
        this.id = id;
    }

    /**
     * Führt den Effekt der Karte aus und wird beim legen der Karte ausgeführt
     *
     * @param game das Spiel in dem die Karte ausgeführt wird
     */
    public abstract void onPlace(Game game);

    /**
     * Gibt die Id der Karte zurück
     *
     * @return die Id
     */
    public String getId() {
        return id;
    }

}