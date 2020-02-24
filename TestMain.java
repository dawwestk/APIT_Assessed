
public class TestMain {

	public static void main(String[] args) {
		Deck deck = new Deck(1);
		
		printDeck(deck);

	}
	
	public static void printDeck(Deck d) {
		for(Card c : d) {
			System.out.print(c);
			System.out.print(" = " + c.getValue() + "\n");
		}
	}

}
