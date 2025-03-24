public class CardList extends List<Card> {
    private int length;
    
    public CardList() {}
    
    public int getLength() {
        return length;
    }
    
    public Card ziehen() {
        toLast();
        Card card = getContent();
        remove();
        length--;
        
        return card;
    }
    
    public void mischen() {
        
    }
    
    public Card[] gibTopDrei() {
        return new Card[3];
    }
    
    public void insertAt(int stelle) {
    
    }
}