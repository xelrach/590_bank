public class Branch_Server_Process {

	public static Branch_Server thisServerProcess;

        public static void main(String args[]) throws Exception  {

                System.out.println("Server started for " + args[0]);

                thisServerProcess = new Branch_Server(args[0], Integer.parseInt(args[1]));
	
		thisServerProcess.processID = Integer.parseInt(args[2]);
		thisServerProcess.branch.processID = Integer.parseInt(args[2]);

		if (thisServerProcess.branch.ServPort < 7000)
			thisServerProcess.branch.is_master = true;
		else
			thisServerProcess.branch.GUIPort = 0;

                String inNeighborsStr = args[3];
                String[] inNeighbors = inNeighborsStr.split(",");

                for (int i = 0; i < inNeighbors.length; i++) {
                        if (inNeighbors[i].length() < 2)
                                continue;                
                        String[] namePort = inNeighbors[i].split("=");
                        Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
                        thisServerProcess.addInEdge( addedBranch );
                }

                String outNeighborsStr = args[4];
                String[] outNeighbors = outNeighborsStr.split(",");
                for (int i = 0; i < outNeighbors.length; i++) {
                        if (outNeighbors[i].length() < 2)
                                continue;
                        String[] namePort = outNeighbors[i].split("=");
                        Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
                        thisServerProcess.addOutEdge( addedBranch );
                }

                String peersStr = args[5];
                String[] peers = peersStr.split(",");

                for (int i = 0; i < peers.length; i++) {
                        if (peers[i].length() < 2)
                                continue;

                        String[] namePort = peers[i].split("=");
                        Branch addedPeer = new Branch(args[0], Integer.parseInt(namePort[1]));
			addedPeer.processID = Integer.parseInt(namePort[0]);

			if (addedPeer.processID != thisServerProcess.processID) {
				thisServerProcess.addToCluster( addedPeer );
			}

			// set the master process to be the first peer, since that is how topo will feed it in
			if (thisServerProcess.master_branch == null) {
				thisServerProcess.master_branch = addedPeer;
				System.out.println("Setting master for " + thisServerProcess.processID + " to " + addedPeer.processID);
			}
                }
                thisServerProcess.ready = false;
                thisServerProcess.start();

                System.out.println("Branch server is done.");
        }
}
