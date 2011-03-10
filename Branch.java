public class Branch {
	public String name;
	String server;
	int ServPort = 12345;
	int GUIPort = 12345;

	Branch() {
	}

	Branch(Branch other) {
		name = other.name;
		server = other.server;
		ServPort = other.ServPort;
		GUIPort = other.GUIPort;
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

	public boolean equals(Object o) {
		if (o instanceof Branch) {
			Branch other = (Branch)o;
			if (other.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}
}
