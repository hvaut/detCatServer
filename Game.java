
public class Game {
    private String id;              //ID des Spiels
    private PlayerList players;           // Liste der Spieler im Spiel
    private CardList pile;        //Kartenstapel
    private Player turn;            //Spieler, der am Zug ist
    private DCServer server;
    public Game(String pID, DCServer pServer)    //Konstruktor
    {
        id = pID;
        server = pServer;
        players = new PlayerList();
        pile = new CardList();
    }

    public void addPlayer(Player pPlayer)       //Spieler pPlayer hinzufügen
    {
        players.append(pPlayer);
    }

    public void removePlayer(Player pPlayer)    //Spieler pPlayer entfernen
    {
        boolean find = false;
        players.toFirst();
        while(players.hasAccess() && !find){
            if(pPlayer == players.getContent()){
                players.remove();
                find = true;
            }
            players.next();
        }
    }

    public Card popPile()                    //Oberste Karte vom Stapel ziehen (Letzte Karte der Liste entfernen)
    {
        return pile.pull();
    }

    public void changeTurn()                //Der nächste Spieler ist am Zug, wenn aus der Liste herausgelaufen wird, ist wieder der erste am Zug
    {
        turn = players.getNextLeb();
    }

    public String getId()                   //Getter für id
    {
        return id;
    }

    public PlayerList getPlayers()                //Getter für Spielerliste
    {
        return players;
    }

    public CardList getPile()                 //Getter für Kartenstapel
    {
        return pile;
    }

    public Player getTurn()            //Getter für turn
    {
        return turn;
    }
    
    public DCServer getServer()
    {
        return server;
    }

}