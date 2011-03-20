public class Account {
	
	public String id;
	private float balance = 0.0f;

	public Account(String id) {
		this.id = id;
		System.out.println("Created account with id " + this.id);
	}

	public Account(Account other) {
		id = other.id;
		balance = other.getBalance();
	}

	public float getBalance() {
		return balance;
	}

	public void addBalance(float amount) {
		balance += amount;
	}

	public String getBranchID() {
		return getBranchID(id);
	}

	public static String getBranchID(String accountID) {
		String branchID = "";

		if (accountID.length() > 2)
			branchID = accountID.substring(0, 2);

		return branchID;
	}

	public boolean equals(Object o) {
		if (o instanceof Account) {
			Account other = (Account)o;
			if (other.id.equals(id)) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}
}
