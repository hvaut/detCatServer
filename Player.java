
/**
 * 
 */
public class Player {

  private String ip;

  private int port;

  private String name;

  public Game game;

  public List<Card> cardlist;

  public void Player(String pIp, int pPort, String pName) {
      ip = pIp;
      port = pPort;
      name = pName;
  }

  public void setGame(Game pGame) {
      game = pGame;
  }

  public void addCard(Card pCard) {
      cardlist.insert(pCard);
  }

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

  public String getIp() {
  return ip;
  }

  public int getPort() {
  return port;
  }

  public String getName() {
  return name;
  }

  public Game getGame() {
  return game;
  }

  public List<Card> getCards() {
  return cardlist;
  }

}