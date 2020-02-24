import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card>{
	
	// Maybe increment number of decks depending on number of players
	private int numberOfDecks;
	
	public Deck(int numberOfDecks) {
		this.numberOfDecks = numberOfDecks;
		buildDeckAndShuffle();
	}
	
	private void buildDeckAndShuffle() {
		buildDeck();
		shuffleDeck();
	}
	
	private void buildDeck() {
		String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
		for(int i = 0; i < numberOfDecks; i++) {
			for(int suitCount = 0; suitCount < suits.length; suitCount++) {
				for(int j = 1; j <= 13; j++) {
					try{
						this.add(new Card(j, suits[suitCount]));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void shuffleDeck() {
		Collections.shuffle(this);
	}
	
}
