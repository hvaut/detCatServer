public abstract class Card {
    private String id;
    public void Card(String id) {
    }
    
    public abstract String getId();

    public abstract void doEf(Game pGame);
}