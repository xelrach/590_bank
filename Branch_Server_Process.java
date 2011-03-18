public class Branch_Server_Process {

	public static Branch_Server thisServerProcess;

        public static void main(String args[]) throws Exception  {

                System.out.println("Server started for " + args[0]);

                thisServerProcess = new Branch_Server(args[0], Integer.parseInt(args[1]));

                String inNeighborsStr = args[2];
                String[] inNeighbors = inNeighborsStr.split(",");
                for (int i = 0; i < inNeighbors.length; i++) {
                        if (inNeighbors[i].length() < 2)
                                continue;                
                        String[] namePort = inNeighbors[i].split("=");
                        Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
                        thisServerProcess.addInEdge( addedBranch );
                }

                String outNeighborsStr = args[3];       
                String[] outNeighbors = outNeighborsStr.split(",");
                for (int i = 0; i < outNeighbors.length; i++) {
                        if (outNeighbors[i].length() < 2)
                                continue;
                        String[] namePort = outNeighbors[i].split("=");
                        Branch addedBranch = new Branch(namePort[0], Integer.parseInt(namePort[1]));
                        thisServerProcess.addOutEdge( addedBranch );
                }

                thisServerProcess.start();

                System.out.println("Branch server is done.");
        }
}
