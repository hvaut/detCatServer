public abstract class Card {

  protected String id;

  public Card(String id) {
  }
  
  public abstract String getId();

  public abstract void doEf(Game game);
}