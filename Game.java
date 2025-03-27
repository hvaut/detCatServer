
public class Game {
    private String id;              //ID des Spiels
    private List players;           // Liste der Spieler im Spiel
    private List<Card> pile;        //Kartenstapel
    private Player turn;            //Spieler, der am Zug ist
    public void Game(String pID)    //Konstruktor
    {
        id = pID;
        players = new List<Player>();
        pile = new List<Card>();
    }

    public void addPlayer(Player pPlayer)       //Spieler pPlayer hinzufügen
    {
        players.append(pPlayer);
    }

    public void removePlayer(Player pPlayer)    //Spieler pPlayer entfernen
    {
        players.toFirst();
        while(players.hasAccess()){
            if(pPlayer == players.getContent()){

                players.remove();
            }
        }
    }

    public Card popPile()                    //Oberste Karte vom Stapel ziehen (Letzte Karte der Liste entfernen)
    {
        pile.toLast();
        Card card = pile.getContent();
        if(pile.hasAccess()){
            pile.remove();
            return card;
        }
        else{return null;}
    }

    public void changeTurn()                //Der nächste Spieler ist am Zug, wenn aus der Liste herausgelaufen wird, ist wieder der erste am Zug
    {
        players.next();
        if(!players.hasAccess()){
            players.toFirst();
        }
    }

    public String getId()                   //Getter für id
    {
        return id;
    }

    public List getPlayers()                //Getter für Spielerliste
    {
        return players;
    }

    public List getPile()                 //Getter für Kartenstapel
    {
        return pile;
    }

    public Player getTurn()            //Getter für turn
    {
        return turn;
    }

}