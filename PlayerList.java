/**
 * Playerlist
 * 
 * @author Jannes
 * @version 19.03.25
 */
public class PlayerList extends List<Player> {

    private Player aktPlayer;

    private int anzPlayers;

    /**
     * Add a card to the end of the list
     * 
     * @param pContent card object to add
     */
    @Override
    public void append(Player pContent) {
        if (pContent != null && !this.isEmpty()) this.anzPlayers++;
        super.append(pContent);
    }

    /**
     * Insert a card before  the current pointer
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
     * Remove a card at the current pointer
     */
    @Override
    public void remove() {
        if (this.hasAccess() && !this.isEmpty()) {
            this.anzPlayers--;
            super.remove();
        }
    }
    
    public Player getAkt() {
        return aktPlayer;
    }

    public String getPlayers() {
        toFirst();
        String erg = "";
        while(hasAccess()){
            erg = erg + getContent().getName() + " ";
            next();
        }
        return erg;
    }

    public Player getNext() {
        toFirst();
        while(hasAccess()){
            if (aktPlayer == getContent()) break;
            next();
        }
        next();
        if (!hasAccess()) toFirst();
        aktPlayer = getContent();
        return aktPlayer;
    }

    public int getAnzahl() {
        return anzPlayers;
    }

    public Player getNextLeb() {
        Player next = getNext();
        while (next != null && !next.getAlive())
            next = getNext();
        aktPlayer = next;
        return aktPlayer;
    }

}