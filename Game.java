import java.util.List;

public class Game {

    private String id;

    private String ip;

    private int port;

    private List players;

    private List pile;

    private Player turn;

    public Game(String pID, DCServer pDCServer)
    {
        id = pID;
    }

    public void addPlayer(Player player) 
    {

    }

    public void removePlayer(Player player) 
    {

    }

    public Card popPile() 
    {
        return null;
    }

    public Player changeTurn()
    {
        return null;
    }

    public String getId() 
    {
        return null;
    }

    public PlayerList getPlayers() 
    {
        return null;
    }

    public CardList getPile()
    {
        return null;
    }

    public Player getTurn() {
        return null;
    }

}