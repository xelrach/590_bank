import java.io.*;
import java.net.*;
import java.util.*;


public class Branch_Server_Process {

	public String name = "some branch";
	public int port = 4444;
	public ServerThread serverThread = new ServerThread();
	public Branch branch = new Branch();

	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	public HashMap<String, Branch> branches = new HashMap<String, Branch>();
	private NetworkWrapper messages;

	public Branch_Server_Process(String name, int port) {
		this.name = name;
		this.port = port;

		branch.name = name;
		branch.ServPort = port;
		branch.GUIPort = 12345;

		System.out.println("Branch name: " + name);
		serverThread.port = port;
		serverThread.name = name;
		

		System.out.println("branches is " + branches.toString());
		messages = new NetworkWrapper(branches);
	}


	public void addComm(Branch branch) {
		if (branch == null || branches.containsKey(branch.name) || this.name == branch.name) {
			System.out.println("Not adding link");
			return;
		}

		System.out.println("Adding link from " + this.name + " to " + branch.name);
		branches.put(branch.name, branch);
		return;
	}

	public void start() {
		if (serverThread != null) {
			Thread thread = new Thread(serverThread);
			thread.start();
		}
		System.out.println("Done starting branch " + branch.name);
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
				answer = transfer(messageID, accountID, arg4, Float.parseFloat(arg5));
			else if (command.equals("q"))
				answer = query(accountID);
		}

		answer = "s" + messageID + " " + answer;
		return answer;
	}

	public String withdrawal(String accountID, float amount) {
		String answer = "error";

		if (!getBranchFromAccountID(accountID).equals(this.name))
			return "wrongbranch";

		checkOrCreateAccount(accountID);

		if (amount < 0) {
			answer = "error-invalid";
			return answer;
		}

		Account account = accounts.get(accountID);

		if (account == null)
			return answer;

		account.addBalance(-amount);
	
		answer = "ok";

		return answer;
	}

	public String getBranchFromAccountID(String accountID) {
		String branchID = "";

		if (accountID.length() > 2)
			branchID = accountID.substring(0, 2);

		return branchID;
	}

	public String deposit(String accountID, float amount) {
		String answer = "error";

		System.out.println("deposit");

		if (!getBranchFromAccountID(accountID).equals(this.name))
			return "wrongbranch " + getBranchFromAccountID(accountID) + ", " + this.name;

		checkOrCreateAccount(accountID);
		

		if (amount < 0) {
			answer = "error-invalid";
			return answer;
		}

		Account account = accounts.get(accountID);

		if (account == null)
			return answer;

		account.addBalance(amount);
	
		answer = "ok";

		return answer;
	}

	public boolean validAccount(String accountID) {
		return validAccount(accountID, false);
	}

	public boolean validAccount(String accountID, boolean mustBeLocal) {
		String branchID = getBranchFromAccountID(accountID);
		if (!mustBeLocal && !branchID.equals(this.name)) {
			System.out.println("account " + accountID + " is in different branch (" + branchID + ", this is " + this.name + " ... do we have access?");
			if (branches.containsKey(branchID)) {
				System.out.println("Yes, because neighbors are " + messages.whoNeighbors());
				return true; // remote branch that this one can talk to
			}
				System.out.println("No, neighbors are " + messages.whoNeighbors());
		}

		Account account = accounts.get(accountID);

		return account != null;
	}

	public String transfer(String messageID, String srcAccountID, String dstAccountID, float amount) {
		String answer = "error";

		if (!getBranchFromAccountID(srcAccountID).equals(this.name))
			return "wrongbranch " + getBranchFromAccountID(srcAccountID) + ", " + this.name;

		// make sure source account exists locally
		checkOrCreateAccount(srcAccountID);

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

		/*
		if (accountFrom.getBalance() < amount) {
			answer = "error-insufficient";
			return answer;
		}
		*/

		accountFrom.addBalance(-amount);
		if (validAccount(dstAccountID, true)) {
			System.out.println("Local transfer of " + amount + " from " + srcAccountID + " to " + dstAccountID);
			accountTo.addBalance(amount);
		} else {
			System.out.println("Transfering " + amount + " to " + dstAccountID);
			sendTransfer(dstAccountID, messageID, amount);
		}

		answer = "ok";

		return answer;
	}

	public void sendTransfer(String accountID, String messageID, float amount) {
		String branchID = getBranchFromAccountID(accountID);
		String message = "";
		message = "c" + messageID + " d " + accountID + " " + amount;
		messages.send( branchID, message );
	}


	public String query(String accountID) {
		String answer = "error";

		Account account = null;

		if (!getBranchFromAccountID(accountID).equals(this.name))
			return "wrongbranch";

		checkOrCreateAccount(accountID);

		account = accounts.get(accountID);

		if (account == null)
			return answer;

		answer = "q" + " " + accountID + " " + Float.toString(account.getBalance());

		return answer;
 	}

	public void checkOrCreateAccount(String accountID) {
		if (!validAccount(accountID)) {
			Account account = new Account(accountID);
			accounts.put(accountID, account);
		}
	}


}


class ServerThread implements Runnable {
  protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean serverRunning = true;

    public String name;
    public int port;
    public Branch_Server_Process thisBranch;

    public ServerThread() {
    }

    public ServerThread(Branch_Server_Process branch) {
	this(branch, "ServerThread", 4444);
    }

    public ServerThread(Branch_Server_Process branch, int port) {
	this(branch, "ServerThread", port);
    }

    public ServerThread(Branch_Server_Process branch, String name, int port) { 
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
		
		System.out.println("Sending: " + dString);

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

		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
        }
	System.out.println("Closing socket.");
        socket.close();
    }
}




class NetworkWrapper {
	public HashMap<String, Branch> topology = new HashMap<String, Branch>();

	public NetworkWrapper(HashMap<String, Branch> topology) {
		this.topology = topology;
	}

	public boolean send(String branchID, String message) {

		if (!topology.containsKey(branchID)) {
			return false;
		}

		Branch branch = topology.get(branchID);

		if (branch == null)
			return false;

		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = message.getBytes();

			System.out.println("Sending (to branch): " + message);

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, branch.ServPort);
			clientSocket.send(sendPacket);
			clientSocket.close();
		} catch( Exception e ) {
			return false;
		}

		return true;
	}

	public String whoNeighbors() {
		System.out.println("topology is " + topology.toString());

		String result = "neighbors: ";
		
		Object[] keys = topology.keySet().toArray();
		String key;
		for (int i = 0; i < keys.length; i++) {
			key = (String)keys[i];

			result = result + key + " ";	
		}

		return result;
	}



}