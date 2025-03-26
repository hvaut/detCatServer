public class SkipCard extends Card {
    String id;
    public SkipCard(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void doEf(Game pGame) {
        pGame.chargeTurn(); //nextTurn();
    }
}