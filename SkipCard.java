public class SkipCard extends Card {

    public SkipCard() {
        super("SKIP");
    }

    public void onPlace(Game game) {
        // go to the next turn
        game.changeTurn();
        // send protocol
        game.getPlayers().toFirst();
        while (game.getPlayers().hasAccess()) {
            Player current = game.getPlayers().getContent();
            game.getServer().send(current.getIP(), current.getPort(), "TURN " + game.getTurn().getName());
            game.getPlayers().next();
        }
    }

}