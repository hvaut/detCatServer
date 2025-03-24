
/**
 * 
 */
public class Player {

    private String ip; // Die IP des Spielers

    private int port; // Der Port des Spielers

    private String name; // Der Name des Spielers

    public Game game; //Das Game des Spielers

    public List<Card> cardlist; // Die Liste, welche alle Karten des Spielers beeinhaltet

    public boolean alive = true;  //Boolean ob der Spieler noch am Leben ist oder nicht
    /**
     * Allgemeine Playerklasse, welche ein Playerobjekt mit den Parametern IP, Port und Name erstellt
     */
    public void Player(String pIp, int pPort, String pName) { 
        ip = pIp;
        port = pPort;
        name = pName;
    }

    /**
     * Setzt das per Parameter gegebene Game als aktuelles Game des Spielers
     */
    public void setGame(Game pGame) { 
        game = pGame;
    }

    /**
     * Fügt eine per Parameter gegebene Karte in die Liste der Karten des Spielers ein
     */
    public void addCard(Card pCard) { 
        cardlist.insert(pCard);
    }

    /**
     * Entfernt die per Parameter gegebene Carde aus der Liste
     */
    public boolean removeCard(Card pCard) { 
        boolean istRemoved = false;
        cardlist.toFirst();
        while(cardlist.hasAccess()){
            if(cardlist.getContent() == pCard){
                cardlist.remove(); 
                istRemoved = true;
            }
            cardlist.next();
        }
        return istRemoved;
    }

    /**
     * Gibt die IP des Spielers zurück
     */
    public String getIp() { 
        return ip;
    }

    /**
     * Gibt den Port des Spielers zurück
     */
    public int getPort() { 
        return port;
    }

    /**
     * Gibt den Namen des Spielers zurück
     */
    public String getName() { 
        return name;
    }

    /**
     * Gibt das aktuelle Games des Spielers zurück
     */
    public Game getGame() { 
        return game;
    }

    /**
     * Gibt die Liste der Karten des Spielers zurück
     */
    public List<Card> getCards() { 
        return cardlist;
    }

    /**
     * Gibt ein true zurück, wenn der Spieler noch lebt und ein false wenn er nicht mehr lebt
     */
    public boolean getAlive(){ 
        return alive;
    }

    /**
     * Setzt den Lebensstatus des Spielers auf den gegebenen Boolean Wert true oder false
     */
    public void setAlive(boolean pStatus){ 
        alive = pStatus;
    }
}