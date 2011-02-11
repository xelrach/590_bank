import java.util.ArrayList;

public class Branch_Server {
	public static void main(String[] args) {
		String name="", port="", gui_ip="", gui_port="";
		ArrayList<Branch> branches = new ArrayList<Branch>();

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
			if (args[i].equals("--gui") || args[i].equals("-g")) {
				String[] branch = args[i+1].split(":");
				gui_ip = branch[0];
				gui_port = branch[1];
				i++;
				continue;
			}
			if (args[i].equals("--branch") || args[i].equals("--remote") || args[i].equals("-r")) {
				System.out.println(args[i+1]);
				String[] branch = args[i+1].split(":");
				Branch b = new Branch(branch[0], branch[1], Integer.parseInt(branch[2].trim()) );
				branches.add(b);
				i++;
				continue;
			}
		}
/*		System.out.println("Server");
		System.out.println(name);
		System.out.println(port);
		System.out.println(gui_ip);
		System.out.println(gui_port);*/
		String str_id = "Server: " + name + ":" + port + " " + gui_ip + ":" + gui_port;
		System.out.println(str_id);

		System.out.println("Other Branches (" + branches.size() + ")");
		for (int i = 0; i<branches.size(); i++) {
			System.out.println(branches.get(i));
		}
	}
}
