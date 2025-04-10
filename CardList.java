import java.util.Random;

/**
 * List to save exclusively Objects from type Card
 *
 * @author Tim Göllner
 * @version 1.0
 */
public class CardList extends List<Card> {

    private int length;

    @Override
    public void append(Card pContent) {
        if (pContent != null && !this.isEmpty()) this.length++;
        super.append(pContent);
    }

    @Override
    public void insert(Card pContent) {
        if (pContent != null && this.hasAccess() || this.isEmpty()) {
            this.length++;
            super.insert(pContent);
        }
    }

    @Override
    public void remove() {
        if (this.hasAccess() && !this.isEmpty()) {
            this.length--;
            super.remove();
        }
    }

    /**
     * Setzt eine Karte an einer bestimmten Stelle ein
     *
     * @param card  die einzusetzende Karte
     * @param index die Stelle des einsetzens
     */
    public void insert(Card card, int index) {
        this.toFirst();
        // if the index is at the end of the list the card will be appended
        if (this.length >= index) {
            this.append(card);
            return;
        }
        // go to the index
        for (int i = 0; i < index; i++) {
            this.next();
        }
        // insert the card if possible else add it
        if (this.hasAccess()) {
            this.insert(card);
        } else {
            this.append(card);
        }
    }

    /**
     * Sortiert die Karten in einer zufälligen Reihenfolge
     */
    public void shuffle() {
        Random random = new Random();
        Card[] array = new Card[this.length];
        // put all cards in this list into the array
        this.toFirst();
        for (int i = 0; i < array.length; i++) {
            if (this.hasAccess()) {
                array[i] = this.getContent();
                this.remove();
            }
        }
        // shuffle the array (Fisher–Yates shuffle)
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // swap cards randomly
            Card card = array[index];
            array[index] = array[i];
            array[i] = card;
        }
        // put the array back into this list
        for (Card card : array) {
            this.append(card);
        }
    }

    /**
     * Gibt die obersten Karten im Stapel zurück
     *
     * @param count die Anzahl an Karten
     * @return das Array mit den obersten Karten
     */
    public Card[] getTop(int count) {
        Card[] cards = new Card[count];
        // go to the start of the top cards
        this.toFirst();
        for (int i = 0; i < (length - count); i++) {
            this.next();
        }
        // put all top cards inside the array
        for (int i = 0; i < count; i++) {
            cards[i] = this.getContent();
            this.next();
        }
        return cards;
    }

    /**
     * Gibt die oberste Karte zurück und entfernt diese
     *
     * @return die oberste Karte
     */
    public Card pull() {
        this.toLast();

        Card card = this.getContent();
        this.remove();

        return card;
    }

    /**
     * Gibt die Anzahl an Karten zurück
     *
     * @return die Anzahl der Karten
     */
    public int getLength() {
        return length;
    }

}