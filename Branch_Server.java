import java.io.*;
import java.net.*;
import java.util.*;

public class Branch_Server {

	public int port = 4444;
	public ServerThread serverThread;

	public HashMap<String, Account> accounts = new HashMap<String, Account>();

	public String process_input(String input) {
		String answer = "";

		String[] tokens = input.split(" ");
		String messageID = "";

		char messageType = 0;
		String command = "";
		String accountID = "";
		String arg4 = "";
		String arg5 = "";

		if (tokens.length > 0) {
			messageID = tokens[0];

			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}

			messageType = messageID.charAt(0);
			messageID = messageID.substring(1); // strip message type character
		}

		if (tokens.length > 1)
			command = tokens[1];

		if (tokens.length > 2)
			accountID = tokens[2];

		if (tokens.length > 3)
			arg4 = tokens[3];

		if (tokens.length > 4)
			arg5 = tokens[4];

		if (messageType == 'c') {
			// client request

			System.out.println("Command is [" + command + "]");

			if (command.equals("w"))
				answer = withdrawal(accountID, Float.parseFloat(arg4));
			else if (command.equals("d"))
				answer = deposit(accountID, Float.parseFloat(arg4));
			else if (command.equals("t"))
				answer = transfer(accountID, arg4, Float.parseFloat(arg5));
			else if (command.equals("q"))
				answer = query(accountID);
		}

		answer = "s" + messageID + " " + answer;
		return answer;
    	}

	public String withdrawal(String accountID, float amount) {
		String answer = "error";

		if (!validAccount(accountID)) {
			answer = "error-account";
			return answer;
		}

		if (amount < 0) {
			answer = "error-invalid";
			return answer;
		}

		Account account = accounts.get(accountID);
		account.addBalance(-amount);
	
		answer = "ok";

		return answer;
	}


	public String deposit(String accountID, float amount) {
		String answer = "error";

		System.out.println("deposit");

		if (!validAccount(accountID)) {
			answer = "error-account";
			return answer;
		}

		if (amount < 0) {
			answer = "error-invalid";
			return answer;
		}

		Account account = accounts.get(accountID);
		account.addBalance(amount);
	
		answer = "ok";

		return answer;
	}

	public boolean validAccount(String accountID) {
		System.out.println("Checking validity of [" + accountID + "]");

		Account account = accounts.get(accountID);

		if (account == null)
			System.out.println("tried to get account [" + accountID + "] but it's null.");

		return account != null;
    	}

	public String transfer(String accountFromID, String accountToID, float amount) {
		String answer = "error";

		if (!validAccount(accountFromID)) {
			answer = "error-from";
			return answer;
		}

		if (!validAccount(accountToID)) {
			answer = "error-to";
			return answer;
		}

		Account accountFrom = accounts.get(accountFromID);
		Account accountTo = accounts.get(accountToID);

		if (amount < 0) {
			answer = "error-invalid";
			return answer;
		}

		if (accountFrom.getBalance() < amount) {
			answer = "error-insufficient";
			return answer;
		}

		accountFrom.addBalance(-amount);
		accountTo.addBalance(amount);

		answer = "ok";

		return answer;
	}

	public String query(String accountID) {
		String answer = "error";

		Account account = null;

		if (!validAccount(accountID)) {
			account = new Account(accountID);
			System.out.println("Creating account with id [" + accountID + "] putting in " + account);
			accounts.put(accountID, account);
		}

		answer = "q" + " " + accountID + " " + account.getBalance();

		return answer;
 	}

}


class ServerThread implements Runnable {
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;

    public int port;


    public ServerThread(int port) throws IOException {
	this("ServerThread", port);
    }

    public ServerThread(String name, int port) throws IOException {
	this.port = port;
        socket = new DatagramSocket(port);

        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }
    }


    public void run() {

        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];
                byte[] inbuf = new byte[256];

                    // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

		
		// figure out response
		String input = new String(buf);
                String dString = process_input( input );

		
		

                buf = dString.getBytes();

		    // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
		moreQuotes = false;
            }
        }
	System.out.println("Closing socket.");
        socket.close();
    }

}
