import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

/* Snapshot provides a summary of all of the accounts in a branch.
 * This is used both for taking a global snapshot and for transfering state.
 */
class Snapshot {
	class Transfer {
		Account source;
		Account destination;
		float amount;

		public Transfer(Account src, Account dest, float moneys) {
			source = src;
			destination = dest;
			amount = moneys;
		}

		public Transfer(Transfer t) {
			source = t.source;
			destination = t.destination;
			amount = t.amount;
		}
	}

	int local_time;
	ArrayList<Account> accounts;
	ArrayList<Transfer> transfers = new ArrayList<Transfer>();
	HashSet<Branch> markers = new HashSet<Branch>();
	public Branch origin;
	public int number;
	
	Snapshot (int time, ArrayList<Account> accnts, Branch originBranch, int snapshotNumber) {
		local_time = time;
		accounts = new ArrayList<Account>();
		for (Account a : accnts) {
			accounts.add(new Account(a));
		}
		accounts = accnts;
		origin = originBranch;
		number = snapshotNumber;
	}

	/**
	 * Called when a marker message is received
	 */
	void addMarker(Branch sourceBranch) {
		if (sourceBranch != null) {
			markers.add(sourceBranch);
		}
	}

	/**
	 * Called to notify the snapshot of a transfer
	 */
	void addTransfer(Account source, Account destination, float amount) {
		transfers.add(new Transfer(source, destination, amount));
	}

	public boolean equals(Object o) {
		if (o instanceof Snapshot) {
			Snapshot other = (Snapshot)o;
			if (other.origin.equals(origin) && other.number==number) {
				return true;
			}
		}
		return false;
	}

	ArrayList<Account> getAccounts() {
		return accounts;
	}

	String getMessage() {
		String result = origin.name + "." + number + " " + local_time + " b";
		for (Account account : accounts) {
			result += " " + account.id + " " + account.getBalance();
		}
		result += " p";
		for (Transfer t : transfers) {
			result += " " + t.source.id + " " + t.destination.id + " " + t.amount;
		}
		return result;
	}

	public String getName() {
		return getName(origin, number);
	}
	
	public static String getName(Branch branch, int number) {
		return branch.name + "." + number;
	}

	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * Returns true if all of the marker messages have been received
	 */
	public boolean isFinished(Set<Branch> inEdges) {
		inEdges.removeAll(markers);
		if (inEdges.isEmpty()) {
			return true;
		}
		return false;
	}

	public static Snapshot parseSnap(String str) {
		Pattern pattBasic = Pattern.compile("(\\w+)\\.-?(\\d+) (\\d+) b(( \\d+\\.\\d+ \\d+.?\\d*)*) p(( \\d+\\.\\d+ \\d+\\.\\d+ \\d+.?\\d*)*)");
		str = str.trim();
		Matcher matches = pattBasic.matcher(str);
		matches.find();
		String origin = matches.group(1);
		int snapNumber = Integer.parseInt(matches.group(2));
		int time = Integer.parseInt(matches.group(3));
		String[] balances = matches.group(4).trim().split(" ");
		String[] transfers = matches.group(6).trim().split(" ");
		ArrayList<Account> accounts = new ArrayList<Account>();

		if (balances.length>1) {
			for (int i=0; i<balances.length; i+=2) {
				accounts.add( new Account(balances[i], Float.parseFloat(balances[i+1])) );
			}
		}
		Snapshot result = new Snapshot(time, accounts, new Branch(origin), snapNumber);
		if (transfers.length>1) {
			for (int i=0; i<transfers.length; i+=3) {
				result.addTransfer(new Account(transfers[i]), new Account(transfers[i+1]), Float.parseFloat(transfers[i+2]));
			}
		}
		return result;
	}
}
