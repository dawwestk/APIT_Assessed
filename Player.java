import java.util.ArrayList;

public class Player {
	
	private String name;
	private int tokens;
	private boolean dealer;
	private ArrayList<Card> hand;
	private int total;
	
	public Player(String n) {
		name = n;
		tokens = 100;
		dealer = false;
		hand = new ArrayList<Card>();
		total = 0;
	}
	
	public void resetHand() {
		hand.clear();
		total = 0;
	}
	
	public void addToHand(Card c) {
		hand.add(c);
		total += c.getValue();
	}
	
	public void payTheWinner(int t, Player p) {
		this.loseTokens(t);
		p.winTokens(t);
	}
	
	private void loseTokens(int t) {
		tokens -= t;
	}
	
	private void winTokens(int t) {
		tokens += t;
	}
	
	public int getTokens() {
		return tokens;
	}
	
	public String getName() {
		return name;
	}
	
	public void setIsDealer() {
		dealer = true;
	}
	
	public boolean getIsDealer() {
		return dealer;
	}
	
}
