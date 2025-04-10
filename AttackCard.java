public class AttackCard extends Card {

    public AttackCard() {
        super("ATTACK");
    }

    @Override
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
        // remove one of the next cards to attack the next turn
        Card[] cards = game.getPile().getTop(2);
        // check if there are at two top cards
        if (cards.length == 3) {
            // check whether the top cards are bombs
            if (!(cards[2] instanceof DetCatCard)) {
                // remove the top card
                game.getPile().toLast();
                game.getPile().remove();
            } else if (!(cards[1] instanceof DetCatCard)) {
                // remove the top card temporarily
                Card card = game.getPile().pull();
                // remove the second top card
                game.getPile().toLast();
                game.getPile().remove();
                // add the first top card back
                game.getPile().append(card);
            }
        }
    }

}
