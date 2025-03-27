public abstract class Card {
  public Card(String id) {
  }
  
  public abstract String getId();

  public abstract void doEf(Game game);
}