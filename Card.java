import java.io.IOException;

public class Card {

	private String card;
	private String suit;
	private int value;
	
	public Card(int i, String s) throws IOException{
		value = i;
		suit = s;
		if(i >= 2 && i <= 10) {
			card = Integer.toString(i);
		} else if(i == 11) {
			card = "Jack";
			value = 10;
		} else if(i == 12) {
			card = "Queen";
			value = 10;
		} else if(i == 13) {
			card = "King";
			value = 10;
		} else if(i == 1) {
			card = "Ace";
			value = 11;
		} else {
			throw new IOException();
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return this.card + " of " + this.suit;
	}
	
}
