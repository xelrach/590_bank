import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.*;
import java.net.*;
import java.util.*;

public class topology {
        private static String filePath;
        private static int startingPort = 3000;
        private static HashMap<String,Branch> alreadyCreatedServerList = new HashMap<String,Branch>();
        
        /**
         * Invokes method on the Branch class to create one branch server.      
         @param serverName A string containing the unique name of the server.
         @param serverPort The server port on which to are going to bind this server instance.
         */
        private static void makeBranchServer(String serverName, int serverPort) {
                        Branch theNewServer = new Branch(serverName, serverPort);
                        // Store [String name,Branch theNewServer] in the alreadyCreatedServerList
                        alreadyCreatedServerList.put(serverName, theNewServer);
                        System.out.println("Started up branch server " + serverName);
        }
        
        private static void allowOneDirectionalCommunication(String fromServer, String toServer) {
                Branch branchServerFrom = alreadyCreatedServerList.get(fromServer);
                Branch branchServerTo = alreadyCreatedServerList.get(toServer);

                branchServerFrom.addOutEdge(branchServerTo);
		branchServerTo.addInEdge(branchServerFrom);
                System.out.println("Allowed communication from " + fromServer + " to " + toServer);
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
                String[] serversOnCurrentLine = currentLine.split(" ");
                
                // Loop through 
                for (int i = 0; i < serversOnCurrentLine.length; i++) {
			String currentServer = serversOnCurrentLine[i];

			if (null == alreadyCreatedServerList.get(currentServer)) {
				makeBranchServer(currentServer, startingPort++);
                        }

			if (i > 0) {
				// Allow communication from first server on current line to second server on current line
				allowOneDirectionalCommunication(serversOnCurrentLine[0],serversOnCurrentLine[i]);
			}
                }
            }
            

	String exec_string = "";
	String exec_gui_string = "";

	for (Map.Entry<String, Branch> branch : alreadyCreatedServerList.entrySet()) {
		String key = branch.getKey();
		Branch thisBranch = branch.getValue();

		exec_string = "java Branch_Server_Process " +  thisBranch.name + " " + thisBranch.ServPort + " " + thisBranch.getBranches();
		System.out.println(exec_string);
		Process p = Runtime.getRuntime().exec( exec_string );

		exec_gui_string = "java ATMGUI 127.0.0.1 " + thisBranch.ServPort + " " + thisBranch.name;
		System.out.println(exec_gui_string);
		Process p2 = Runtime.getRuntime().exec( exec_gui_string );
	}      


            theFis.close();
            theDis.close();
            theReader.close();
		System.out.println("Done reading topology file.");
        }

        /**
         * Calls the makeTopology() method.    
         @param args[0] The absolute path to the file which specifies branch topology.
         */
        public static void main(String args[]) throws Exception  {
                        makeTopology(args[0]);
//                try {
//                } catch (Exception e) {}
                
                while (true) {
                	try {
                		java.lang.Thread.currentThread();
						Thread.sleep(3600000);
                	} catch (Exception e) {
                		// e.printStackTrace();
                	}
                }
        }
}
