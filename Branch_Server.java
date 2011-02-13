import java.io.*;
import java.net.*;
import java.util.*;

public class Branch_Server {

	public String name = "some branch";
	public int port = 4444;
	public ServerThread serverThread = new ServerThread(this);

	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	public HashMap<String, Branch_Server> branches = new HashMap<String, Branch_Server>();

	public Branch_Server(String name, int port) {
		this.name = name;
		this.port = port;

		serverThread.port = port;
		serverThread.name = name;
	}

	public void addComm(Branch_Server branch) {
		
		if (branches.containsKey(branch.name) || this.name == branch.name)
			return;

		branches.put(branch.name, branch);

		return;
	}

	public void start() {
		if (serverThread != null) {
			Thread thread = new Thread(serverThread);
			thread.start();
		}
	}

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

	public String getBranchFromAccountID(String accountID) {
		String branchID = "";

		if (accountID.length() > 2)
			branchID = accountID.substring(0, 1);

		return branchID;
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
		return validAccount(accountID, false);
	}

	public boolean validAccount(String accountID, boolean mustBeLocal) {
		String branchID = getBranchFromAccountID(accountID);
		if (!mustBeLocal && branchID != this.name) {
			if (branches.containsKey(branchID))
				return true; // remote branch that this one can talk to
		}

		Account account = accounts.get(accountID);

		return account != null;
    	}

	public String transfer(String srcAccountID, String dstAccountID, float amount) {
		String answer = "error";

		// make sure source account exists locally
		if (!validAccount(srcAccountID, true)) {
			answer = "error-from";
			return answer;
		}

		// make sure destination account exists or belongs to a branch that can be contacted
		if (!validAccount(dstAccountID)) {
			answer = "error-to";
			return answer;
		}

		Account accountFrom = accounts.get(srcAccountID);
		Account accountTo = accounts.get(dstAccountID);

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
    protected boolean serverRunning = true;

    public String name;
    public int port;
    public Branch_Server thisBranch;

    public ServerThread(Branch_Server branch) {
	this(branch, "ServerThread", 4444);
    }

    public ServerThread(Branch_Server branch, int port) {
	this(branch, "ServerThread", port);
    }

    public ServerThread(Branch_Server branch, String name, int port) { 
	this.thisBranch = branch;
	this.name = name;
	this.port = port;
    }


    public void run() {
	this.port = port;

	try {
		socket = new DatagramSocket(port);
	} catch(Exception e) {

	}

        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }

        while (serverRunning) {
            try {
                byte[] buf = new byte[256];
                byte[] inbuf = new byte[256];

                    // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

		
		// figure out response
		String input = new String(buf);
                String dString = thisBranch.process_input( input );

		
		

                buf = dString.getBytes();

		    // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
		serverRunning = false;
            }
        }
	System.out.println("Closing socket.");
        socket.close();
    }

}
