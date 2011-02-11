mport java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import BranchServer;

public class topology {
	private static String filePath;
	private static int startingPort = 3000;
	private static HashMap alreadyCreatedServerList = new HashMap();
	
	/**
	 * Invokes methods on the BranchServer class to create either one or two branch servers.       
	 @param communicateFrom  The first server specified on some line "i" of the input file
	 @param communicateTo The second server specific on line "i"
	 @param serverPort The server port on which to are going to bind this server instance.
	 */
	private static void makeBranchServer(String communicateFrom, String communicateTo, int serverPort) {
		if (null == communicateFrom) { // This means we just need to create a new server with no connections.
			BranchServer theNewServer = new BranchServer(communicateTo, serverPort);
			theNewServer.start();
			alreadyCreatedServerList.put(communicateTo, theNewServer);
		} else {
			BranchServer theNewServer = new BranchServer(communicateFrom, communicateTo, serverPort);
			theNewServer.start();
			alreadyCreatedServerList.put(communicateFrom, theNewServer);
		}
	}
	
	private static void allowOneDirectionalCommunication(String fromServer, String toServer) {
		BranchServer branchServerFrom = alreadyCreatedServerList.get(fromServer);
		BranchServer branchServerTo = alreadyCreatedServerList.get(toServer);
		branchServerFrom.addComm(branchServerTo);
	}
	
	/**
	 * Creates the various BranchServers based upon the relationships specified in inputFilePath.              
	 @param  inputFilePath The absolute path to the file which specifies branch topology.
	 @throws IOException
	 */
	public static void makeTopology(String inputFilePath) throws IOException {
		filePath = inputFilePath;

	    FileInputStream theFis = new FileInputStream(filePath);
	    DataInputStream theDis = new DataInputStream(theFis);
	    BufferedReader theReader = new BufferedReader(new InputStreamReader(theDis));
	    
	    String currentLine;
	    while ((currentLine = theReader.readLine()) != null) {
	    	String[] theNode = currentLine.split(" ");
	    	String canSend = theNode[0];
	    	String canReceiveFromSender = theNode[1];
	    	
	    	// If needed, create second server first, so that the reference exists when makeBranchServer() needs it.
	    	if (null == alreadyCreatedServerList.get(theNode[1])) {
	    		makeBranchServer(null,theNode[1],startingPort++);
	    	}
	    	if (null == alreadyCreatedServerList.get(theNode[0])) {
	    		// Server hasn't been created.  Start 'er up!
	    		makeBranchServer(theNode[0],theNode[1],startingPort++);
	    		
	    	} else {
	    		// Allow server corresponding to first parameter to communicate with server corresponding to second parameter
	    		allowOneDirectionalCommunication(theNode[0],theNode[1]);
	    	}
	    }
	    
	    theFis.close();
	    theDis.close();
	    theReader.close();
	}

	/**
	 * Calls the makeTopology() method.    
	 @param args[0] The absolute path to the file which specifies branch topology.
	 */
	public static void main(String args[]) {
		makeTopology(args[0]);
	}
}
