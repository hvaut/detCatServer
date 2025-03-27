public class DefuseCard extends Card  {
    
    public DefuseCard() {
        super("");
    }
    
    public String getId() {
        return null;
    }
    
    public void doEf(Game pGame) {
        pGame.changeTurn(); //nextTurn();

    }
}