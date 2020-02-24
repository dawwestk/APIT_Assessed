import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

	// Class variables for the parent - ChatServer (implements Runnable)
	private ServerSocket server;
	private ArrayList<ClientRunner> clients = new ArrayList<ClientRunner>();
	private static int PORT = 8888;

	// Set up a ServerSocket
	public Server() {
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// When run...
	public void run() {
		while (true) {
			Socket clientSocket = null;
			try {
				// Wait for connection...
				clientSocket = server.accept();
				System.out.println("New player connected");
				
				// Make new client runner object
				ClientRunner client = new ClientRunner(clientSocket, this);
				
				// Add to ArrayList
				clients.add(client);
				
				// Start their thread (allow them to take input)
				new Thread(client).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Transmits message to all ClientRunner objects in the ArrayList
	public void transmit(Message m) {
		for (ClientRunner c : clients) {
			if (c != null) {
				c.transmitMessage(m);
			}
		}
	}

	public static void main(String[] args) {
		// main - create a new Thread to contain the ChatServer (which is runnable)
		Thread t = new Thread(new Server());
		
		// Call the ChatServer run method - open, wait for connections, add to arraylist
		t.start();
		
		// Pauses this thread (main) until t has finished operation
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Make a private class with Runnable
		private class ClientRunner implements Runnable {
			private Socket s = null;
			private Server parent = null;	// parent object is also runnable
			private ObjectInputStream inputStream = null;
			private ObjectOutputStream outputStream = null;

			// On creation assign socket and parent
			public ClientRunner(Socket s, Server parent) {
				this.s = s;
				this.parent = parent;
				try {
					// try to get each input/output stream objects from socket
					outputStream = new ObjectOutputStream(this.s.getOutputStream());
					inputStream = new ObjectInputStream(this.s.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void run() {
				// receive messages
				try {
					// Open and close input stream based on input
					Message message = null;
					while ((message = (Message) inputStream.readObject()) != null) {
						// parent has transmit method
						this.parent.transmit(message);
					}
					inputStream.close();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// Receives message from parent (the message transmitted to all in ClientRunner arraylist
			// outputs it to the Stream, then to the page
			public void transmitMessage(Message m) {
				try {
					outputStream.writeObject(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
}