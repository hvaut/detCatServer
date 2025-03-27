
public class Game {
    private String id;
    private List players;
    private List<Card> pile;
    private Player turn;
    public void Game(String pID)
    {
        id = pID;
        players = new List<Player>();
        pile = new List<Card>();
    }

    public void addPlayer(Player pPlayer) 
    {
        players.append(pPlayer);
    }

    public void removePlayer(Player pPlayer) 
    {
        players.toFirst();
        while(players.hasAccess()){
            if(pPlayer == players.getContent()){

                players.remove();
            }
        }
    }

    public Card popPile() 
    {
        pile.toLast();
        Card card = pile.getContent();
        if(pile.hasAccess()){
            pile.remove();
            return card;
        }
        else{return null;}
    }

    public void changeTurn()
    {
        players.next();
        if(!players.hasAccess()){
            players.toFirst();
        }
    }

    public String getId() 
    {
        return id;
    }

    public List getPlayers() 
    {
        return players;
    }

    public List getPile()
    {
        return pile;
    }

    public Player getTurn() {
        return turn;
    }

}