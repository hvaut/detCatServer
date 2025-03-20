
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

  public void Player(String pIp, int pPort, String pName) { //Allgemeine Playerklasse, welche ein Playerobjekt mit den Parametern IP, Port und Name erstellt
      ip = pIp;
      port = pPort;
      name = pName;
  }

  public void setGame(Game pGame) { //Setzt das per Parameter gegebene Game als aktuelles Game des Spielers
      game = pGame;
  }

  public void addCard(Card pCard) { // Fügt eine per Parameter gegebene Karte in die Liste der Karten des Spielers ein
      cardlist.insert(pCard);
  }

  public boolean removeCard(Card pCard) { //Entfernt die per Parameter gegebene Carde aus der Liste
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

  public String getIp() { //Gibt die IP des Spielers zurück
  return ip;
  }

  public int getPort() { //Gibt den Port des Spielers zurück
  return port;
  }

  public String getName() { //Gibt den Namen des Spielers zurück
  return name;
  }

  public Game getGame() { //Gibt das aktuelle Games des Spielers zurück
  return game;
  }

  public List<Card> getCards() { //Gibt die Liste der Karten des Spielers zurück
  return cardlist;
  }

  public boolean getAlive(){ //Gibt ein true zurück, wenn der Spieler noch lebt und ein false wenn er nicht mehr lebt
      return alive;
  }
  
  public void setAlive(boolean pStatus){ //Setzt den Lebensstatus des Spielers auf den gegebenen Boolean Wert true oder false
       alive = pStatus;
  }
}