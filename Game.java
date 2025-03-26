import java.util.List;

public class Game {

    private String id;

    private String ip;

    private int port;

    private List players;

    private List pile;

    private Player turn;

    public void Game(String pID,String pIP, int pPort)
    {
        id = pID;
        ip = pIP;
        port = pPort;
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

    public Player chargeTurn()
    {
        return null;
    }

    public int getId() 
    {
        return 0;
    }

    public List getPlayers() 
    {
        return null;
    }

    public List getPile()
    {
        return null;
    }

    public List getTurn() {
        return null;
    }

}