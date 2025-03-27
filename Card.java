public abstract class Card {
  String id;
  /**
   * Der Konstruktor soll die IDs der jeweiligen Karten-Typen festlegen (z.B. "CAT")
   */
  public Card(String id) {
      this.id = id;
  }
  
  /**
   * Gibt die Id zurück
   */
  public abstract String getId();

  /**
   * führt den jeweiligen Effekt aus
   */
  public abstract void doEf(Game game);
}