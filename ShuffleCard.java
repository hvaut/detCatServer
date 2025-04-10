public class ShuffleCard extends Card {

    public ShuffleCard() {
        super("SHUFFLE");
    }

    @Override
    public void onPlace(Game game) {
        // shuffle the pile
        game.getPile().shuffle();
    }

}
