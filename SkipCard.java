public class SkipCard extends Card {
    
    public SkipCard() {
        super("");
    }
    
    public String getId() {
        return null;
    }
    

    public void doEf(Game pGame) {
        pGame.changeTurn(); //nextTurn();
    }
}