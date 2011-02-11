import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class topology {
	private String filePath;
	
	public topology(String inputFilePath) throws IOException {
		super();
		this.filePath = inputFilePath;

	    FileInputStream theFis = new FileInputStream(filePath);
	    DataInputStream theDis = new DataInputStream(theFis);
	    BufferedReader theReader = new BufferedReader(new InputStreamReader(theDis));
	    
	    String currentLine;
	    while ((currentLine = theReader.readLine()) != null) {
	    	String[] theNode = currentLine.split(" ");
	    	String canSend = theNode[0];
	    	String canReceiveFromSender = theNode[1];
	    }
	    
	    theFis.close();
	    theDis.close();
	    theReader.close();
	}

}

