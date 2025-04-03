/**
 * Erweiterung der Klasse List speziell für Player.
 * 
 * @author Jannes
 * @version 02.04.25
 */
public class PlayerList extends List<Player> {

    private Player aktPlayer; //Der Aktuelle Spieler wird hier gespeichert

    private int anzPlayers; //Die Anzahl an Spielern wird hier gespeichert

    /**
     * Add a player to the end of the list
     * 
     * @param pContent card object to add
     */
    @Override
    public void append(Player pContent) {
        if (pContent != null && !this.isEmpty()) this.anzPlayers++;
        super.append(pContent);
    }

    /**
     * Insert a player before the current pointer
     * 
     * @param pContent card object to insert
     */
    @Override
    public void insert(Player pContent) {
        if (pContent != null && this.hasAccess() || this.isEmpty()) {
            this.anzPlayers++;
            super.insert(pContent);
        }
    }

    /**
     * Remove a player at the current pointer
     */
    @Override
    public void remove() {
        if (this.hasAccess() && !this.isEmpty()) {
            this.anzPlayers--;
            super.remove();
        }
    }

    /**
     * gibt den aktuellen Spieler an.
     */
    public Player getAkt() {
        return aktPlayer;
    }

    /**
     * gibt alle Spielernamen an in einem String an. 
     * Wenn keine Spieler existieren gibt es einen leeren string zurück.
     */
    public String getPlayers() {
        toFirst();
        String erg = "";
        while(hasAccess()){
            erg = erg + getContent().getName() + " ";
            next();
        }
        return erg;
    }

    /**
     * gibt den nächsten Spieler nach dem aktuellen Spieler an egal ob tot oder am leben.
     * wenn keiner existiert gibt er null zurück.
     */
    public Player getNext() {
        toFirst();
        if (aktPlayer == null) {
            aktPlayer = getContent();
            return getContent();
        }
        while(hasAccess()){
            if (getContent() == aktPlayer){
                next();
                if (!hasAccess()){
                    toFirst();
                }
                aktPlayer = getContent();
                return getContent();
            }
            next();
        }
        aktPlayer = null;
        return null;
    }

    /**
     * gibt die Anzahl der Spieler an.
     */
    public int getAnzahl() {
        return anzPlayers;
    }

    /**
     * gib den nächsten lebenden Spieler an nach dem aktuellen Spieler.
     * wenn keiner existiert gibt er null zurück.
     */
    public Player getNextLeb() {
        toFirst();
        if (aktPlayer == null) {
            aktPlayer = getContent();
            if (getContent().getAlive()) {
                return getContent();
            }
        }
        while(hasAccess()){
            if (getContent() == aktPlayer){
                next();
                if (!hasAccess()) {
                    toFirst();
                }
                while (hasAccess() && !getContent().getAlive()) {
                    if (getContent() == aktPlayer) {
                        aktPlayer = null;
                        return null;
                    }
                    next();
                    if (!hasAccess()) {
                        toFirst();    
                    }
                }
                aktPlayer = getContent();
                return getContent();
            }
            next();
        }
        aktPlayer = null;
        return null;
    }

    /**
     * Entfernt den Angegeben Spieler.
     */
    public void removePlayer(Player pp){
        toFirst();
        while(hasAccess()){
            if (getContent() == pp){
                remove();
            }
            next();
        }
    }

}