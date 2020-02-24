
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Has action listener to allow for button use
public class Client extends JFrame implements ActionListener {

	// Swing Chat Client variables
	private Socket server = null;
	private JTextField gameInfo;
	private JLabel infoLabel;
	private ObjectOutputStream outputStream;
	private JTextField messageField;
	private JButton sendButton;
	private JButton hitButton;
	private JButton standButton;
	private Player player;
	private static int PORT = 8888;

	// constructor needs usual Swing methods
	public Client() {
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Welcome to 21");
		
		// get Name from an input pane
		String name = JOptionPane.showInputDialog(this, "What's your name?");
		player = new Player(name);
		
		// Make a panel to add things to
		JPanel panel = new JPanel(new BorderLayout());
		this.add(panel);
		
		// topPanel will contain game information in a text box
		JPanel topPanel = new JPanel();
		gameInfo = new JTextField(20);
		infoLabel = new JLabel("Info: ");
		topPanel.add(infoLabel);
		topPanel.add(gameInfo);
		
		panel.add(topPanel, BorderLayout.NORTH);
		
		// mainPanel will contain the players view and the opponent view
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2));
		
		// myCard contains buttons for user input
		JPanel myCard = new JPanel();
		myCard.setLayout(new BorderLayout());
		myCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// cardFace will show the card values the user has been dealt
		JPanel cardFace = new JPanel();
		cardFace.setBackground(Color.red);
		
		myCard.add(cardFace, BorderLayout.CENTER);
		
		// cardButtons will handle user input
		JPanel cardButtons = new JPanel();
		cardButtons.setLayout(new GridLayout(0, 2));
		hitButton = new JButton("Hit");
		standButton = new JButton("Stand");
		hitButton.addActionListener(this);
		standButton.addActionListener(this);
		cardButtons.add(hitButton);
		cardButtons.add(standButton);
		myCard.add(cardButtons, BorderLayout.SOUTH);
		
		// The table is the view for the remaining opponent information
		JPanel table = new JPanel();
		table.setBackground(Color.blue);
		
		mainPanel.add(myCard);
		mainPanel.add(table);
		
		panel.add(mainPanel, BorderLayout.CENTER);
		
		// Add bottom panel with input text and button
		JPanel bottomPanel = new JPanel();
		messageField = new JTextField(40);
		sendButton = new JButton("Send");
		
		// Dont forget to add action listener (this)
		sendButton.addActionListener(this);
		bottomPanel.add(messageField);
		bottomPanel.add(sendButton);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		
		// Once panels are added, make visible
		this.setVisible(true);
		
		// connect method instantiates the Socket object to connect
		// to the localhost on the port provided
		connect();
		try {
			// try to initialise output stream of socket (which was just connected)
			outputStream = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create a read worker (Swing Worker) to do background tasks
		// this is the parent
		ReadWorker rw = new ReadWorker(server, this);
		
		// execute kicks off doInBackground
		rw.execute();
		
		// print message to the terminal to show connection
		System.out.println("HERE");
	}

	// helper method to connect to server Socket
	private void connect() {
		try {
			server = new Socket("127.0.0.1", PORT);
			System.out.println("Connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// adds the message to the text display
	public void display(Message m) {
		gameInfo.setText(m.toString());
	}
	
    

	// listens for action on sendButton
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			String messageText = messageField.getText();
			try {
				// tries to write message to Message object
				// writes to Output Stream (output from my window to "other")
				outputStream.writeObject(new Message(messageText, player.getName()));
				// reset the text in the input box to be blank
				messageField.setText("");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else if(e.getSource() == hitButton) {
			String hitText = "hit";
			try {
				// tries to write message to Message object
				// writes to Output Stream (output from my window to "other")
				outputStream.writeObject(new Message(hitText, player.getName()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else if(e.getSource() == standButton) {
			String standText = "stand";
			try {
				// tries to write message to Message object
				// writes to Output Stream (output from my window to "other")
				outputStream.writeObject(new Message(standText, player.getName()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Client();
	}
	
	// ReadWorker extends SwingWorker(Output type, Input type?)
		// ALL client/Server operations should be in a SwingWorker object
		// This is a private class within SwingChatClient
		private class ReadWorker extends SwingWorker<Void, Void> {
			
			// initialise socket, stream and parent to null;
			private Socket socket = null;
			private ObjectInputStream inputStream = null;
			private Client parent;

			// On creation, assign as expected
			// stream is the input stream of the socket
			public ReadWorker(Socket s, Client parent) {
				this.socket = s;
				this.parent = parent;
				try {
					inputStream = new ObjectInputStream(this.socket.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// doInBackground is like run() for normal threads
			// required method of a SwingWorker
			public Void doInBackground() {
				// printed to the client terminal (not the swing window)
				System.out.println("Started read worker");
				Message m = null;
				try {
					// try to read in a message
					while ((m = (Message) inputStream.readObject()) != null) {
						
						// print it to the terminal
						System.out.println(m);
						
						// print it to the Swing window
						parent.display(m);
					}
					
					// need ClassNotFoundException when using Serializable objects
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// returning null as Void was output type
					return null;
				}
			}
		}
}