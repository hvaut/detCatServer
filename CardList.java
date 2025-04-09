import java.util.Random;

/**
 * List to save exclusively Objects from type Card
 * 
 * @author Tim Göllner
 * @version 1.0
 */
public class CardList extends List<Card> {
    private int length;

    /**
     * Add a card to the end of the list
     * 
     * @param pContent card object to add
     */
    @Override
    public void append(Card pContent) {
        if (pContent != null && !this.isEmpty()) this.length++;
        super.append(pContent);
    }

    /**
     * Insert a card before the current pointer
     * 
     * @param pContent card object to insert
     */
    @Override
    public void insert(Card pContent) {
        if (pContent != null && this.hasAccess() || this.isEmpty()) {
            this.length++;
            super.insert(pContent);
        }
    }

    /**
     * Remove a card at the current pointer
     */
    @Override
    public void remove() {
        if (this.hasAccess() && !this.isEmpty()) {
            this.length--;
            super.remove();
        }
    }

    /**
     * Insert a card at the specified index
     * 
     * @param card card object to insert
     * @param index index for the insertion
     */
    public void insert(Card card, int index) {
        this.toFirst();

        if (this.length == index) {
            this.append(card);
            return;
        }

        for (int i = 0; i < index; i++) {
            if (!this.hasAccess()) throw new IndexOutOfBoundsException("index out of bounds");
            this.next();
        }

        this.insert(card);
    }

    /**
     * Get and remove the top card of the list
     */
    public Card pull() {
        this.toLast();

        Card card = this.getContent();
        this.remove();

        return card;
    }

    /**
     * Randomly shuffle the cards
     */
    public void shuffle() {
        Random random = new Random();
        Card[] array = new Card[this.length];

        this.toFirst();

        for (int i = 0; i < this.length; i++) {
            if (this.hasAccess()) {
                array[i] = this.getContent();
                this.remove();
            }
        }

        for (int i = 0; i < array.length - 1; i++) {
            int index = random.nextInt(i + 1);

            Card card = array[index];
            array[index] = array[i];
            array[i] = card;
        }

        for (Card card : array) this.append(card);
    }

    /**
     * Get the top n cards from the list
     * 
     * @param count number of cards to get
     */
    public Card[] getTop(int count) {
        Card[] cards = new Card[count];

        this.toFirst();
        
        for (int i = 0; i < (length - count); i++) {
            if (!this.hasAccess()) throw new IndexOutOfBoundsException("index out of bounds");
            this.next();
        }

        for (int i = 0; i < count; i++) {
            if (!this.hasAccess()) throw new IndexOutOfBoundsException("index out of bounds");

            cards[i] = this.getContent();

            this.next();
        }

        return cards;
    }

    /**
     * Get the length of the list
     */
    public int getLength() {
        return length;
    }

}