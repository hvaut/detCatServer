/**
 * Erweiterung der Klasse List speziell für Player.
 * 
 * @author Jannes
 * @version 02.04.25
 */
public class PlayerList extends List<Player> {

  private Player aktPlayer; //Der Aktuelle Spieler wird hier gespeichert
  
  private int anzPlayers; //Die Anzahl an Spielern wird hier gespeichert

  
  public void Playerlist() {}
  
  @Override
  public void insert(Player pContent){
      super.insert(pContent);
      
      if (pContent != null){
          if (anzPlayers == 0){
              aktPlayer = pContent;
          }
          anzPlayers++;
      }
  }
  
  @Override
  public void append(Player pContent) {
      super.append(pContent);
      
      if (pContent != null){
          if (anzPlayers == 0){
              aktPlayer = pContent;
          }
          anzPlayers++;
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
          erg = erg + getContent().getName();
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
      while(hasAccess()){
          if (getContent() == aktPlayer){
              next();
              if (hasAccess()){
                  return getContent();
              }
              else return null;
            }
          next();
          
  }
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
  while(hasAccess()){
      if (getContent() == aktPlayer){
          next();
          if (hasAccess() && getContent().getAlive() == true){
              return getContent();
          }
        }
      next();
  }
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
              anzPlayers--;
          }
          next();
      }
  }

}