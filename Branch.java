public class Branch {
	String name;
	String server;
	int ServPort = 12345;
	int GUIPort = 12345;

	Branch() {
	}

	Branch(String s_name, String s_server, int n_servport, int n_guiport) {
		name = s_name;
		server = s_server;
		ServPort = n_servport;
		GUIPort = n_guiport;
	}

	public String toString() {
		return name+":"+server+":"+ServPort+":"+GUIPort;
	}
}
