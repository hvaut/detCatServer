/**
 * Erweiterung der Klasse List speziell für Player.
 *
 * @author Jannes
 * @version 02.04.25
 */
public class PlayerList extends List<Player> {

    private Player turn;
    private int length;

    @Override
    public void append(Player pContent) {
        if (pContent != null && !this.isEmpty()) this.length++;
        super.append(pContent);
    }

    @Override
    public void insert(Player pContent) {
        if (pContent != null && this.hasAccess() || this.isEmpty()) {
            this.length++;
            super.insert(pContent);
        }
    }

    @Override
    public void remove() {
        if (this.hasAccess() && !this.isEmpty()) {
            this.length--;
            super.remove();
        }
    }

    /**
     * Gibt den nächsten lebenden Spieler zurück und setzt ihn zeitgleich
     *
     * @return der nächste lebende Spieler
     */
    public Player getNext() {
        toFirst();
        // if there is no current player return the first
        if (turn == null) {
            turn = getContent();
            // make sure the player is alive
            if (getContent().isAlive()) {
                return getContent();
            }
        }
        while (hasAccess()) {
            // check if the player is the current turn
            if (getContent() == turn) {
                next();
                // if we are out of bounds go to the start
                if (!hasAccess()) {
                    toFirst();
                }
                // walk through the list until we find a living player
                while (hasAccess() && !getContent().isAlive()) {
                    // if we are at our start point return null
                    if (getContent() == turn) {
                        turn = null;
                        return null;
                    }
                    next();
                    // if we are out of bounds go to the start
                    if (!hasAccess()) {
                        toFirst();
                    }
                }
                // set the current player and return
                turn = getContent();
                return getContent();
            }
            next();
        }
        // set the current player to null and return
        turn = null;
        return null;
    }

    /**
     * Gibt alle Spieler in einem String zurück
     *
     * @return der String mit allen Spielern
     */
    public String getString() {
        toFirst();
        String string = "";
        while (hasAccess()) {
            string = string + getContent().getName() + " ";
            next();
        }
        return string;
    }

    /**
     * Gibt den Spieler zurück, der aktuelle am Zug ist
     *
     * @return der Spieler am Zug
     */
    public Player getTurn() {
        return turn;
    }

    /**
     * Gibt die Anzahl an Spielern zurück
     *
     * @return die Anzahl an Spielern
     */
    public int getLength() {
        return length;
    }

}