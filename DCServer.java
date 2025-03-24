public class DCServer extends Server {

    private List<Player> players;
    private List<Game> games;

    public DCServer(int port) {
        super(port);

        players = new List<>();
        games = new List<>();
    }

    public void processNewConnection(String ip, int port) {
    }

    public void processClosingConnection(String ip, int port) {
        Player player = getPlayer(ip, port);

        if (player != null) {
            removePlayer(player);
        }
    }

    public void processMessage(String ip, int port, String message) {
        String[] data = message.split(" ");

        // TODO
    }

    public void addGame(Game game) {
        games.append(game);
    }

    public void removeGame(Game game) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId() == game.getId())
                games.remove();
            games.next();
        }
    }

    public Game getGame(int id) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId() == id)
                return games.getContent();
            games.next();
        }
        return null;
    }

    public void addPlayer(Player player) {
        players.append(player);
    }

    /**
     * Methode removePlayer entfernt player aus spielerliste und allen Games
     *
     * @param player der Spieler der abgemeldet werden soll
     */
    public void removePlayer(Player player) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(player.getIp()) && players.getContent().getPort() == player.getPort()){
                players.remove();
                while(games.hasAccess()){
                    games.getContent().removePlayer(players.getContent());
                    games.next();}
            }
            players.next();
        }
    }

    /**
     * Methode getPlayer
     *
     * @param ip des gesuchten Spielers
     * @param port des gesuchten Spielers
     * @return Der Spieler mit den gesuchten Verbindungsdaten
     */
    public Player getPlayer(String ip, int port) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(ip) && players.getContent().getPort() == port)
                return players.getContent();
            players.next();
        }
        return null;
    }

    /**
     * Methode getPlayer
     *
     * @param name des gesuchten Spielers
     * @return der Spieler mit dem gesuchten Namen
     */
    public Player getPlayer(String name) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getName().equalsIgnoreCase(name))
                return players.getContent();
            players.next();
        }
        return null;
    }

    /**
     * Methode getGames
     *
     * @return alle Games auf dem Server
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * Methode getPlayers
     *
     * @return gibt alle zurzeit angemeldeten Spieler zurück
     */
    public List<Player> getPlayers() {
        return players;
    }

}