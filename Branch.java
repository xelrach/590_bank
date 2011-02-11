public class Branch {
	String name;
	String server;
	int port;

	Branch(String s_name, String s_server, int n_port) {
		name = s_name;
		server = s_server;
		port = n_port;
	}

	public String toString() {
		return name+":"+server+":"+port;
	}
}
