public class SeeFutureCard extends Card {

    public SeeFutureCard() {
        super("FUTURE");
    }

    @Override
    public void onPlace(Game game) {
        // get the top 3 cards
        Card[] cards = game.getPile().getTop(3);
        // create card string
        String string = "";
        for (Card card : cards) {
            string += card.getId() + " ";
        }
        // send protocol message
        game.getServer().send(game.getTurn().getIP(), game.getTurn().getPort(), "FUTURE " + string);
    }

}
