public class Branch_Server_Process {

	public static Branch_Server thisServerProcess;

	public static void main(String args[]) throws Exception  {


		System.out.println("Server started for " + args[0]);

		thisServerProcess = new Branch_Server(args[0], Integer.parseInt(args[1]));

		thisServerProcess.branch.GUIPort = Integer.parseInt(args[2]);

		thisServerProcess.processID = Integer.parseInt(args[3]);
		thisServerProcess.branch.processID = Integer.parseInt(args[3]);

		System.out.println("Process " + thisServerProcess.branch.processID + " GUIPort is " +  thisServerProcess.branch.GUIPort);

		if (thisServerProcess.branch.ServPort < 7000) {
			thisServerProcess.branch.is_master = true;
			thisServerProcess.master_branch = thisServerProcess.branch;
		}

		String inNeighborsStr = args[4];
		String[] inNeighbors = inNeighborsStr.split(",");

		for (int i = 0; i < inNeighbors.length; i++) {
			if (inNeighbors[i].length() < 2)
				continue;
			String[] namePort = inNeighbors[i].split("=");
			Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
			thisServerProcess.addInEdge( addedBranch );
		}

		String outNeighborsStr = args[5];
		String[] outNeighbors = outNeighborsStr.split(",");
		for (int i = 0; i < outNeighbors.length; i++) {
			if (outNeighbors[i].length() < 2)
				continue;
			String[] namePort = outNeighbors[i].split("=");
			Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
			thisServerProcess.addOutEdge( addedBranch );
		}

		String peersStr = args[6];
		String[] peers = peersStr.split(",");

		// Let this branch server know about all of its initial peers
		for (int i = 0; i < peers.length; i++) {
			if (peers[i].length() < 2)
				continue;

			String[] namePort = peers[i].split("=");
			Branch addedPeer = new Branch(args[0], Integer.parseInt(namePort[1]));
			addedPeer.processID = Integer.parseInt(namePort[0]);
			addedPeer.ServPort = Integer.parseInt(namePort[1]);

			//System.out.println("Adding peer process for " + thisServerProcess.processID + ": " + namePort[0] + " port " + namePort[1]);

			if (addedPeer.processID != thisServerProcess.processID) {
				thisServerProcess.addToCluster( addedPeer );
			}

			// set the master process to be the first peer, since that is how topo will feed it in
			if (thisServerProcess.master_branch == null) {
				thisServerProcess.master_branch = addedPeer;
				//System.out.println("Setting master for " + thisServerProcess.processID + " to " + addedPeer.processID);
			}
		}

		thisServerProcess.start();

		System.out.println("Branch server is done.");
	}
}
