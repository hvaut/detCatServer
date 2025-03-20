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

    public Game getGame2(Player player) {
        return player.getGame();
    }

    public void addPlayer(Player player) {
        players.append(player);
    }

    public void removePlayer(Player player) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp() == player.getIp() && players.getContent().getPort() == player.getPort())
                players.remove();
            players.next();
        }
    }

    public Player getPlayer(String ip, int port) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(ip) && players.getContent().getPort() == port)
                return players.getContent();
            players.next();
        }
        return null;
    }

    public Player getPlayer2(String name) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getName().equalsIgnoreCase(name))
                return players.getContent();
            players.next();
        }
        return null;
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Player> getPlayer() {
        return players;
    }

}