public class GUI_Server_Process {

	public static ATMGUI thisServerProcess;

        public static void main(String args[]) throws Exception  {

                System.out.println("GUI server started for " + args[0]);

                new ATMGUI(args[0],args[1],args[2]).setVisible(true);
                
                System.out.println("GUI server is done.");
        }
}
