public class DefuseCard extends Card  {
    String id;
    public DefuseCard(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void doEf(Game pGame) {
        pGame.chargeTurn(); //nextTurn();
    }
}