public class Account {
	
	public String id;
	private float balance = 0;

	public Account(String id) {
		this.id = id;
		System.out.println("Created account with id " + this.id);
	}

	public float getBalance() {
		return balance;
	}

	public void addBalance(float amount) {
		balance += amount;
	}
}
