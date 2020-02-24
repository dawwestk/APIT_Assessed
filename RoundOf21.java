import java.util.ArrayList;
import java.util.Random;

public class RoundOf21 {
	
	/*
	 * Rules:
	 * 	- if ANY player has blackjack - declare immediately, they win DOUBLE stakes from everyone
	 *  - if two or more players have blackjack - no money is paid between them, but the player
	 *  	with "positional advantage" wins everyone else's money
	 *  - if a non-dealer has blackjack, they become the dealer (otherwise, dealer rotates)
	 *  - dealer plays clock-wise, players can hit or stand
	 *  - as many hits as desired until 21 is exceeded - then stake is paid to the dealer
	 *  - dealer pays (from his pot) if people have cards under 21, but greater than his
	 *  - dealer receives from each player who goes bust, or has cards less than the dealer value
	 */
	
	private Deck deck;
	private ArrayList<Player> players;
	
	public RoundOf21(ArrayList<Player> players) {
		this.players = players;
		deck = new Deck(players.size());
		chooseDealer();
	}
	
	private void chooseDealer() {
		Random rand = new Random();
		int r = rand.nextInt(players.size());
		players.get(r).setIsDealer();
	}
	
	
	
}
