public class DefuseCard extends Card  {
    /**
     * für Kommentare siehe in Klasse Card
     */
    public DefuseCard() {
        super("DEFUSE");
    }
    
    public String getId() {
        return id;
    }
    
    public void doEf(Game pGame) {
        pGame.changeTurn(); //nextTurn();
    }
}