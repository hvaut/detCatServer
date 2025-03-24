/**
 * Playerlist
 * 
 * @author Jannes
 * @version 19.03.25
 */
public class PlayerList extends List<Player> {

  private Player aktPlayer;
  
  private int anzPlayers;

  
  public void Playerlist() {}

  public Player getAkt() {
  return aktPlayer;
  }

  public String getPlayers() {
  toFirst();
  String erg = "";
  while(hasAccess()){
      erg = erg + getContent().getName();
      next();
  }
  return erg;
  }

  public Player getNext() {
  toFirst();
  while(hasAccess()){
      //if (){}
  }
  return getContent();
  }

  public int getAnzahl() {
  return anzPlayers;
  }

  public Player getNextLeb() {
  return null;
  }

}