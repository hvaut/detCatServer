
public class Game {
    private String id;              //ID des Spiels
    private PlayerList players;           // Liste der Spieler im Spiel
    private CardList pile;        //Kartenstapel
    private Player turn;            //Spieler, der am Zug ist
    public Game(String pID, DCServer server)    //Konstruktor
    {
        id = pID;
        players = new PlayerList();
        pile = new CardList();
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

}