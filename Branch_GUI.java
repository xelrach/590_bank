public class Branch_GUI {
	public static void main(String[] args) {
		String name="", port="", branch_ip="", branch_port="";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--name") || args[i].equals("-n")) {
				name = args[i+1];
				i++;
				continue;
			}
			if (args[i].equals("--port") || args[i].equals("-p")) {
				port = args[i+1];
				i++;
				continue;
			}
			if (args[i].equals("--server") || args[i].equals("--branch") || args[i].equals("-b")) {
				String[] branch = args[i+1].split(":");
				branch_ip = branch[0];
				branch_port = branch[1];
				i++;
				continue;
			}
		}
/*		System.out.println("GUI");
		System.out.println(name);
		System.out.println(port);
		System.out.println(branch_ip);
		System.out.println(branch_port);*/
		String str_id = "GUI: " + name + ":" + port + " " + branch_ip + ":" + branch_port;
		System.out.println(str_id);
	}
}
