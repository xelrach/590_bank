import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Branch_Server {

	boolean ready = false;

	class HeartbeatThread extends Thread {
	    public void run() {

				try {
					sleep(5000);
				} catch (Exception e) {
				}

	
			while (ready == false) {
				try {
					sleep(1000);
				} catch (Exception e) {
				}
			}

	    	while (true) {
	    		if (doHeartbeat == true) {
					if (branch.is_master == true) { // if this branch thinks it's the master
						// send heartbeat to all peers
						Map.Entry pairs;
						Iterator it = cluster_peers.entrySet().iterator();
						Branch branch;
						while (it.hasNext()) {
							pairs = (Map.Entry)it.next();
							branch = (Branch)pairs.getValue();
							transmit_alive( branch );
						}
				    } else { // check for heartbeat from who this branch thinks is the master
				    	if (master_is_alive == false) { // OMG no heartbeat
				    		restore_cluster();
				    		continue;
				    	}
				    	/* pretend like master is not alive; because we haven't received a heartbeat during this second yet */
				    	master_is_alive = false; // don't worry; this gets set true in process_input every second (if a heartbeat was received)
				    }
		    	}
	    		
				// now wait a second to allow sending (or receipt) or heartbeat
	    		try {
	    			Thread.sleep(1000);
	    		} catch (Exception e) {
	    		}
	    	}
	    }
	}
	
	public boolean master_is_alive = false;
	public boolean doHeartbeat = true;
	public String name = "some branch";
	private int local_time = 0;
	public int port = 4444;
	public int backupID = 0;
	public ServerThread serverThread;
	public Thread heartbeat;
	public Branch branch = new Branch();
	public int lastSnapshot = 0;
	public int processID = 0;
	Logger log;
	FileHandler fh;

	public Branch master_branch;

	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	public HashMap<String, Branch> inNeighbors = new HashMap<String, Branch>();
	public HashMap<String, Branch> outNeighbors = new HashMap<String, Branch>();
	public Hashtable<Integer, Branch> cluster_peers = new Hashtable<Integer, Branch>(); // <ProcessID, Branch>

	private HashMap<String, Snapshot> snapshots = new HashMap<String, Snapshot>();
	private NetworkWrapper messages;

	public Branch_Server(String name, int port) {		 
		this.name = name;
		this.port = port;
		this.backupID = backupID;

		log = Logger.getLogger(Branch_Server.class.getName());
		fh = null;
		try{
			fh = new FileHandler("Branch_Server." + name + ".log");
		}catch (Exception e) {
			System.err.println(e);
		}
		Logger.getLogger("").addHandler(fh);
//		log.addHandler(fh);
		log.setLevel(Level.ALL);
		Logger.getLogger("").setLevel(Level.ALL);
		fh.setFormatter(new SimpleFormatter());
		branch.name = name;
		branch.ServPort = port;

		branch.GUIPort = port + 1000;
		serverThread =  new ServerThread(this, name, port);
		serverThread.port = port;
		serverThread.name = name;
		messages = new NetworkWrapper(outNeighbors);

		log.log(Level.INFO, "Branch name: " + name + " Port: " + port);
	}

	int getGUIPort() {
		return branch.GUIPort;
	}

	InetAddress getGUIAddress() {
		try {
			return InetAddress.getByName("localhost");
		} catch (Exception e) {
		}
		return null;
	}


	public void addToCluster(Branch branch) {
		log.log(Level.INFO, "Adding process " + branch.processID + " to process " + this.processID);
		cluster_peers.put( new Integer(branch.processID), branch);
	}

	/**
	 * Adds a branch that can be communicated with
	 */
	public void addOutEdge(Branch branch) {
//		log.fine("New Out Edge: " + branch.getName());
		if (branch == null || outNeighbors.containsKey(branch.name) || this.name == branch.name) {
//			System.out.println("Not adding link");
			return;
		}

//		System.out.println("Adding link from " + this.name + " to " + branch.name);
		outNeighbors.put(branch.name, branch);
		return;
	}

	/**
	 * Adds a branch that can communicate with this branch
	 */
	public void addInEdge(Branch branch) {
		log.fine("New In Edge: " + branch.getName());
		if (branch == null || inNeighbors.containsKey(branch.name) || this.name == branch.name) {
			System.out.println("Not adding link");
			return;
		}

		System.out.println("Adding link from " + branch.name + " to " + this.name);
		inNeighbors.put(branch.name, branch);
		return;
	}

	public void start() {
		if (serverThread != null) {
			Thread thread = new Thread(serverThread);
			thread.start();
		}
		log.log(Level.INFO,"Done starting branch " + branch.name);
		heartbeat = new HeartbeatThread();
		heartbeat.start();
		
	}

	public String process_input(String input) throws NoPathException {
//		log.log(Level.INFO,name + " recieved " + input);
		String answer = "";

		String[] tokens = input.split(" ");

		char messageType = 0;
		String command = "";
		String accountID = "";
		String arg4 = "";
		String arg5 = "";
		
		String UpdateMsg = new String();

		if (tokens.length > 0) {
			// messageID = tokens[0];

			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}

			messageType = tokens[0].charAt(0);
			// messageID = messageID.substring(1); // strip message type character
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

			log.log(Level.INFO, branch.processID + " got client command is [" + command + "]");

			if (command.equals("w")) {
				++local_time;
				answer = withdrawal(accountID, Float.parseFloat(arg4));
				UpdateMsg = input;
                                UpdateToBackups(UpdateMsg);
			} else if (command.equals("f")){
                                answer=fakecrash();
                        }else if (command.equals("d")) {
				++local_time;
				answer = deposit(accountID, Float.parseFloat(arg4));
                                    UpdateMsg = input;
                                    UpdateToBackups(UpdateMsg);
			} else if (command.equals("t")) {
				++local_time;
				answer = transfer(accountID, arg4, Float.parseFloat(arg5));
                                    UpdateMsg = input;
                                    UpdateToBackups(UpdateMsg);
                                //transfer function needs changing.
			} else if (command.equals("s")) {
				answer = startSnapshot();
			} else if (command.equals("q")) {
				//Query message
				++local_time;
				answer = query(accountID);
				log.log(Level.INFO, answer);
			} else if (command.equals("k")) {
				//Kill message
                                    UpdateMsg = input;
                                    UpdateToBackups(UpdateMsg);
			    this.serverThread.serverRunning = false;
				System.exit(0);
			}
		} else if (messageType == 's') {
			if (command.equals("m")) {
				answer = markerMessage( tokens[2], tokens[3], tokens[4] );
			} else if (command.equals("t")) {
				++local_time;
				answer = transfer(accountID, arg4, Float.parseFloat(arg5));
			    UpdateMsg = input;
			    UpdateToBackups(UpdateMsg);
                                //transfer function needs changing.
			} else if (command.equals("b")) {
				//log.log(Level.INFO, "Process " + processID  + " received a heartbeat");
				answer = peer_acknowledge( tokens[2] );
			} else if (command.equals("state")) {
				log.log(Level.INFO, input);
				answer = sendState();
				log.log(Level.INFO, "Returning State: " + answer);
			} else if (command.equals("qm")) {
				answer = queryMasterRespond();
//				return answer; // we just want the integer
			}
		}
/*
		if (!answer.equals("ok")) {
			System.out.println("");
			System.out.print("Bad Result: " + answer);
//			return null;
		}*/
		answer = "s" + " " + answer;
		return answer;
	}

        public void UpdateToBackups(String msg) {
		if (!branch.is_master) {
			return;
		}
            Iterator it = cluster_peers.values().iterator();
	    log.log(Level.INFO, "cluster_peers size: "+cluster_peers.size());
            Branch otherBranch;
            while (it.hasNext()) {
                otherBranch = (Branch)it.next();
		log.log(Level.INFO, "Considering forwarding to: "+otherBranch.processID);
                if (otherBranch.is_master) {
			log.log(Level.INFO, "Had is_master. WHY???");
                    continue;
		}
                // fix here
		try{
			log.log(Level.INFO, "Forwarding " + msg + " to " + otherBranch.processID);
	                messages.send(branch,otherBranch,msg,false);
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString());
		}
            }
        }

	public String peer_acknowledge( String ack_id ) {
		String answer = "ok";

//		log.log(Level.INFO, "Process " + processID  + " received peer heartbeat from " + ack_id );

		Branch branch_ack = cluster_peers.get( new Integer(ack_id) );

		if (branch_ack == null) {
			log.log(Level.INFO, "Couldn't find peer for processID " + ack_id);
			return "error";
		}
		if (branch_ack.is_master) {
//			log.log(Level.INFO, "HEARTBEAT RECEIVED from process " + ack_id + " to process " + processID);
			master_is_alive = true; // this branch server has received a heartbeat
		}

//		System.out.println("Process " + processID + " marking " + ack_id + " alive");
		branch_ack.alive(true);
		
		return answer;
	}

	public void restore_cluster() {
		Map.Entry pairs;

//		if (processID != 2)
//			return;

		if (master_branch != null) {
			log.log(Level.INFO, "Process " + processID  + " restoring cluster. Old master was " + master_branch.processID );
		} else {
			log.log(Level.INFO, "Process " + processID  + " restoring cluster. Old master was null?" );
		}

		Branch branch;

		Iterator it = cluster_peers.entrySet().iterator();
		while (it.hasNext()) {
			pairs = (Map.Entry)it.next();
			branch = (Branch)pairs.getValue();

			System.out.println("Process " + processID + " marking " + branch.processID + " dead");
			branch.alive(false);
		}

		it = cluster_peers.entrySet().iterator();
		while (it.hasNext()) {
			pairs = (Map.Entry)it.next();
			branch = (Branch)pairs.getValue();
			log.log(Level.INFO, "Process " + this.processID  + " is sending a peer heartbeat to " + branch.processID);
			transmit_alive( branch );
		}

		try {
			log.log(Level.INFO, "Process " + this.processID  + " is sleeping while waiting for peer heartbeats.");
			doHeartbeat = false;
			Thread.sleep(10000);
			log.log(Level.INFO, "Process " + this.processID  + " is done sleeping after waiting for peer heartbeats.");
		} catch (Exception e) {
		}

		determine_master();
		doHeartbeat = true;
	}

	public void determine_master() {

		Map.Entry pairs;
		Branch newMaster = this.branch;
		
//		log.log(Level.INFO, "Process " + this.processID  + " is determining the new master.");

		Iterator it = cluster_peers.entrySet().iterator();
		Branch branch;
		while (it.hasNext()) {

			pairs = (Map.Entry)it.next();
			branch = (Branch) pairs.getValue();

			branch.is_master = false;

			if (newMaster == null || (branch.alive_marker && branch.processID < newMaster.processID)) {
				log.log(Level.INFO, "Process " + this.processID  + " thinks new master might be " + newMaster.toString());
				newMaster = branch;
			} else {
				if (!branch.alive_marker)
					log.log(Level.INFO, "Process " + this.processID  + ": new master is not " + newMaster.toString() + " because it's dead");
				if ( branch.processID < newMaster.processID )
					log.log(Level.INFO, "Process " + this.processID  + ": new master is not " + newMaster.toString() + " because it's not next in line ");
			}
		}

		log.log(Level.INFO, "Process " + this.processID  + " says the new master is " + newMaster.toString());

		newMaster.is_master = true;
		master_branch = newMaster;
	}

	public void transmit_alive (Branch process) {
		if (this.branch.processID == process.processID) {
			log.log(Level.INFO, "Why is process " + processID + " sending a heartbeat to itself?");
			return;
		}

		if (process == null)
			return;
		try{
			messages.send( this.branch, process, "s b " + this.processID );
		} catch(Exception e) {}
	}

	public String fakecrash(){
		String answer = "error";
		int sleepsecond = 5;
		//sleepsecond += (int)(Math.random() * 106);
		long sleeptime = sleepsecond * 1000;
		try{
			System.out.println("Branch server " + this.name + " sleeps for " + String.format("%d", sleepsecond) + " seconds.");
			local_time = 0;
			doHeartbeat = false;
			accounts.clear();
			Thread.sleep(sleeptime);
		}
		catch(InterruptedException e){
			answer = "Fake crash fails.";
			return answer;
		}
		wakeup();
		answer = "OK";
		return answer;
	}

	public void wakeup(){
		log.log(Level.INFO, "Waking Up " + branch.processID);
		doHeartbeat = true;
		Iterator it = cluster_peers.values().iterator();
		Branch otherBranch = (Branch)it.next();
		master_branch = queryMaster(otherBranch);
		String state = requestState(otherBranch);
		log.log(Level.INFO, state);
		Snapshot snap = Snapshot.parseSnap(state);
		for (Account a:snap.getAccounts()) {
			accounts.put(a.id,a);
		}
		local_time = snap.local_time;
	}

	public String sendState() {
		//Store the state of the branch
		Iterator it = accounts.values().iterator();
		ArrayList<Account> values = new ArrayList<Account>();
		while (it.hasNext()) {
			values.add( new Account( (Account)it.next()) );
		}
		Snapshot snap = new Snapshot(local_time, values, new Branch(name), -1);
		return snap.getMessage();
	}

	Branch queryMaster(Branch otherBranch) {
/*		try{
			return new Socket("localhost", 1234);
		} catch (Exception e) {
		}
		return null;*/
		String sMaster = "";
		log.log(Level.INFO, "Asking " + otherBranch.processID + " who the master is.");
		try{
		sMaster = messages.send(branch, otherBranch, "s qm");
		}catch (Exception e){}
		sMaster = sMaster.trim();
		log.log(Level.INFO, "*" + sMaster.substring(2) + "*");
		return cluster_peers.get( Integer.parseInt(sMaster.substring(2)) );
/*		int primMasterProcessID = Integer.parseInt(sMaster);
		Integer masterProcessID = new Integer(primMasterProcessID);
		return cluster_peers.get(masterProcessID);*/
	}

	String queryMasterRespond() {
		log.log(Level.INFO, branch.processID + " saying master is: " + master_branch.processID);
		return Integer.toString(master_branch.processID);
	}

	public String requestState(Branch otherBranch) {
		String state = "";
		try {
			state = messages.send(branch, otherBranch, "s state");
		} catch (Exception e) {}
		return state;
	}

	/** 
	 * Start a snapshot
	 */
	public String startSnapshot() {
		int snapshotID = lastSnapshot + 1;
		lastSnapshot = snapshotID;
		transmitMarker(this.branch.name, snapshotID);
		handleMarker(null, this.branch, snapshotID);
		return "ok";
	}


	/** 
	 * Handles the reception of a snapshot marker.
	 *
	 * @param sourceBranch The branch that sent the marker.
	 * @param originBranch The branch that started the snapshot.
	 * @param snapshotNumber The origin branch's snapshot number.
	 */
	public void handleMarker(Branch sourceBranch, Branch originBranch, int snapshotNumber) {
//		assert originBranch.getName()=="01";
		String snap_name = Snapshot.getName(originBranch,snapshotNumber);
		Snapshot snap;
		if ( !snapshots.containsKey(Snapshot.getName(originBranch,snapshotNumber)) ) {
			//Store the state of the branch
			Iterator it = accounts.values().iterator();
			ArrayList<Account> values = new ArrayList<Account>();
			while (it.hasNext()) {
				values.add( new Account( (Account)it.next()) );
			}
			snap = new Snapshot(local_time, values, originBranch, snapshotNumber);
			snapshots.put(snap.getName(), snap);
		} else {
			snap = snapshots.get(snap_name);
		}
		String notice = "Storing Marker For " + originBranch.getName() + "." + snapshotNumber;
		if (sourceBranch!=null) {
			notice += " from " + sourceBranch.getName();
		}
		log.info(notice);
		System.out.println(notice);
		snap.addMarker(sourceBranch);
		if ( snap.isFinished( new HashSet<Branch>(inNeighbors.values()) ) ) {
			System.out.println("");
			System.out.println("Snapshot Finsihed");
			sendGUISnapshot(snap);
//			snapshots.remove(snap.getName());
		}
	}

	public boolean snapExists(Branch originBranch, int snapshotNumber) {
		String snap_name = Snapshot.getName(originBranch,snapshotNumber);
//		System.out.print(snap_name);
		if ( snapshots.containsKey(snap_name) ) {
//			System.out.println(" exists");
			return true;
		}
//		System.out.println(" doesn't exist.");
		return false;
	}

	/**
	 * Notifies ongoing snapshots of transfers
	 */
	void notifySnapshots(Account source, Account destination, float amount) {
		for (Snapshot snap : snapshots.values()) {
			snap.addTransfer(source, destination, amount);
		}
	}

	/**
	 * Sends the snapshot information to the GUI
	 */
	void sendGUISnapshot(Snapshot snap) {
		String message = snap.getMessage();
		try {
			serverThread.sendToGUI(message);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public String markerMessage(String arg1, String arg2, String arg3) {
//		System.out.println("");
//		System.out.println(name + " got a marker from: " + arg1 + "w/ origin " + arg2);
		String result = "";

		Branch source = inNeighbors.get(arg1);
		if (source == null)
			return "invalidSource";

		Branch origin = new Branch(arg2);

		int snapshotID = Integer.parseInt(arg3);
		if (!snapExists(origin, snapshotID)) {
			transmitMarker(origin.name, snapshotID);
		}
		handleMarker(source, origin, snapshotID);
		return "ok";
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
		return Account.getBranchID(accountID);
	}

	public String deposit(String accountID, float amount) {
		String answer = "error";

//		System.out.println("deposit");

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
			if (outNeighbors.containsKey(branchID)) {
				System.out.println("Yes, because neighbors are " + messages.whoNeighbors());
				return true; // remote branch that this one can talk to
			}
				System.out.println("No, neighbors are " + messages.whoNeighbors());
		}

		Account account = accounts.get(accountID);

		return account != null;
	}

	public String transfer(String srcAccountID, String dstAccountID, float amount) {
		String answer = "error";
		if (getBranchFromAccountID(srcAccountID).equals(this.name)) {
			// make sure source account exists locally
			checkOrCreateAccount(srcAccountID);
			// make sure destination account exists or belongs to a branch that can be contacted
			if (!validAccount(dstAccountID)) {
				answer = "error-to";
				return answer;
			}
			Account accountFrom = accounts.get(srcAccountID);
			Account accountTo = accounts.get(dstAccountID);
			if (accountTo == null && validAccount(dstAccountID)) {
				accountTo = new Account(dstAccountID);
			}
			if (amount < 0) {
				answer = "error-invalid";
				return answer;
			}
			notifySnapshots(accountFrom, accountTo, amount);
			accountFrom.addBalance(-amount);
			if (validAccount(dstAccountID, true)) {
				System.out.println("Local transfer of " + amount + " from " + srcAccountID + " to " + dstAccountID);
				accountTo.addBalance(amount);
			} else {
				System.out.println("Transferring " + amount + " to " + dstAccountID);
				sendTransfer(accountFrom, accountTo, amount);
			}

			answer = "ok";
		} else if (getBranchFromAccountID(dstAccountID).equals(this.name)) {
			// make sure source account exists locally
			checkOrCreateAccount(dstAccountID);
			Account accountFrom = accounts.get(srcAccountID);
			Account accountTo = accounts.get(dstAccountID);
			if (accountFrom == null) {
				accountFrom = new Account(srcAccountID);
			}
			if (amount < 0) {
				answer = "error-invalid";
				return answer;
			}
			notifySnapshots(accountFrom, accountTo, amount);
			accountTo.addBalance(amount);
			answer = "ok";
		} else {
			return "wrongbranch " + getBranchFromAccountID(srcAccountID) + ", " + this.name;
		}
		return answer;
	}

	public void sendTransfer(Account src, Account dest, float amount) {
                                if (branch.is_master == false) return;
		String branchID = dest.getBranchID();
		String message = "s" + " t " + src.id + " " + dest.id + " " + amount;
		try{
		messages.send( branchID, message );
		} catch (Exception e) {}
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


	/**
	 * Send a marker to all out edges
	 */
	void transmitMarker(String originBranch, int snapshotID) {
		String message = "s m " + name + " " + originBranch + " " + snapshotID;

		try{
		Thread.sleep(4000);
		}catch(Exception e){}
		for (Map.Entry<String, Branch> branch : outNeighbors.entrySet()) {
			String key = branch.getKey();
			Branch outBranch = branch.getValue();
			try{
			messages.send( outBranch.name, message );
			}catch (Exception e) {}
		}
	}

}

class ServerThread implements Runnable {
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean serverRunning = true;

    public String name;
    public int port;
    public Branch_Server thisBranch;
    Logger log = Logger.getLogger(ServerThread.class.getName());

    public ServerThread(Branch_Server branch, String name, int port) { 
    	this.thisBranch = branch;
    	this.name = name;
    	this.port = port;
    }

    public void sendToGUI(String message) throws IOException {
        send(message, thisBranch.getGUIAddress(), thisBranch.getGUIPort());
    }

    public void send(String message, InetAddress address, int port) throws IOException {
    	byte[] buf = new byte[message.length()];
    	buf = message.getBytes();
    	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
//    	log.log(Level.INFO, name + " sending " + message + " to port: " + port);
        socket.send(packet);
    }

    public void run() {
		log.info(name + " is running.");

    	try {
		log.info(name + "." + thisBranch.processID + " is binding to port: " + port);
    		socket = new DatagramSocket(port);
    	} catch(Exception e) {
    		log.log(Level.INFO, "Could not create socket" + e);
    		serverRunning = false;
    	}

        while (serverRunning) {
            try {
                byte[] inbuf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length);
                socket.receive(packet);

        		// figure out response
        		String input = new String(inbuf);
                String dString = thisBranch.process_input(input);

        		if (dString==null) {
        			continue;
        		}
//        		System.out.println("Sending: " + dString);
        		    // send the response to the client at "address" and "port"

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
    		    send(dString,address,port);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.log(Level.INFO, "SERVER FAILURE due to IOException:  " + e);
    		        serverRunning = false;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    log.log(Level.INFO, "SERVER FAILURE due to NullPointerException:  " + e);
    		        serverRunning = false;
    			} catch (NoPathException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.log(Level.INFO, "SERVER FAILURE due to NoPathException:  " + e);
					serverRunning = false;
				}

    		try {
    			Thread.sleep(100);
    		} catch (Exception e) {
    		    serverRunning = false;
    		    log.log(Level.INFO, "SERVER FAILURE during Thread.sleep:  " + e);
    		    System.out.println("Sleep exception caused branch server to crash");
    		}
        }
	    System.out.println("Closing socket.");
        socket.close();
    }

}

class NoPathException extends Exception {
	NoPathException(String s) {
		super(s);
	}
	NoPathException() {}
}

class NetworkWrapper {
	public HashMap<String, Branch> topology = new HashMap<String, Branch>();

	public NetworkWrapper(HashMap<String, Branch> topology) {
		this.topology = topology;
	}

	public String send(String address, int port, String message, boolean waitForResponse) throws NoPathException {
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
		} catch (Exception e) {
			throw new NoPathException("Could not make socket");
		}
		try {
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = message.getBytes();

//			System.out.println("Sending (to branch): " + message);

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			if (waitForResponse) {
				byte[] buff = new byte[65536];
				DatagramPacket response = new DatagramPacket(buff, 100);
				clientSocket.receive(response);
				clientSocket.close();
				return new String(response.getData());
			} else {
				return "";
			}
		} catch( Exception e ) {
			clientSocket.close();
			throw new NoPathException(e.toString());
		}
	}

	public String send(String address, int port, String message) throws NoPathException {
		return send(address, port, message, true);
	}

	public String send( Branch fromProcess, Branch toProcess, String message, boolean waitForResponse) throws NoPathException {
		if (fromProcess.name != toProcess.name)
			return send(toProcess.name, message);

		return send(toProcess.server, toProcess.ServPort, message, waitForResponse);
	}

	public String send( Branch fromProcess, Branch toProcess, String message) throws NoPathException {
		return send(fromProcess, toProcess, message, true);
	}

	public String send(String branchID, String message) throws NoPathException {
		if (!topology.containsKey(branchID)) {
			throw new NoPathException("Branch is not in topology");
		}

		Branch branch = topology.get(branchID);
		if (branch == null)
			throw new NoPathException("Branch is not in topology");

		return send("localhost", branch.ServPort, message);
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
