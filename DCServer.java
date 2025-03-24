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

    /**
     * Methode addGame fügt das Spiel hinzu
     *
     * @param player das Spiel das hinzugefügt werden soll
     */
    public void addGame(Game game) {
        games.append(game);
    }

    /**
     * Methode removeGame entfernt das Spiel aus der Spielliste
     *
     * @param game das Spiel das entfernt werden soll
     */
    public void removeGame(Game game) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId() == game.getId())
                games.remove();
            games.next();
        }
    }
    
    /**
     * Methode getGame gibt das gesuchte Spiel zurück
     *
     * @param id des gesuchten Spiels
     * @return das Spiel mit der gesuchten id
     */
    public Game getGame(int id) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId() == id)
                return games.getContent();
            games.next();
        }
        return null;
    }

    /**
     * Methode addPlayer fügt den Spieler hinzu
     *
     * @param player der Spieler der hinzugefügt werden soll
     */
    public void addPlayer(Player player) {
        players.append(player);
    }

    /**
     * Methode removePlayer entfernt den Spieler aus der Spielerliste und allen Spiellisten
     *
     * @param player der Spieler der entfernt werden soll
     */
    public void removePlayer(Player player) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(player.getIp()) && players.getContent().getPort() == player.getPort()){
                players.remove();
                games.toFirst();
                while (games.hasAccess()) {
                    games.getContent().removePlayer(players.getContent());
                    games.next();
                }
            }
            players.next();
        }
    }

    /**
     * Methode getPlayer gibt den gesuchten Spieler zurück
     *
     * @param ip des gesuchten Spielers
     * @param port des gesuchten Spielers
     * @return der Spieler mit den gesuchten Verbindungsdaten
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
     * Methode getPlayer gibt den gesuchten Spieler zurück
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
     * Methode getGames gibt alle Games zurück
     *
     * @return alle Games auf dem Server
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * Methode getPlayers gibt alle Spieler zurück
     *
     * @return gibt alle aktuell angemeldeten Spieler zurück
     */
    public List<Player> getPlayers() {
        return players;
    }

}