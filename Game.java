
public class Game {
    private String id;                               //id des Spiels
    private List<Player> players;                   //Spielerliste
    private List<Card> pile;                       //Kartenstapel
    private Player turn;                          //Enthält welcher Spieler am Zug ist
    public void Game(String pID)
    {
        id = pID;            
        players = new List<Player>();    
        pile = new List<Card>();       
    }

    public void addPlayer(Player pPlayer)                 //Der Spieler pPlayer wird hinzugefügt
    {
        players.append(pPlayer);
    }

    public void removePlayer(Player pPlayer)             //Wenn die Liste nicht leer ist, wird der Spieler pPlayer entfernt
    {
        players.toFirst();
        while(players.hasAccess()){
            if(pPlayer == players.getContent()){

                players.remove();
            }
        }
    }

    public Card popPile()                                //die oberste Karte des Stapels wird gezogen (Letzte Karte der Liste)
    {
        pile.toLast();
        Card card = pile.getContent();
        if(pile.hasAccess()){
            pile.remove();
            return card;
        }
        else{return null;}
    }

    public void changeTurn()                           //Der nächste Spieler ist dran. Wenn es keinen nächsten Spieler in der Liste gibt, wird wieder an den Anfang gegangen
    {
        players.next();
        if(!players.hasAccess()){
            players.toFirst();
        }
    }

    public String getId()                              //Getter für id
    {
        return id;
    }

    public List getPlayers()                          //Getter für Spielerliste
    {
        return players;
    }

    public List getPile()     //Getter für Kartenstapel
    {
        return pile;
    }

    public Player getTurn()  //Getter dafür wer am Zug ist
    { 
        return turn;
    }

}