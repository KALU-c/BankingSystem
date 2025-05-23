package Bank;

import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

public class SavingsAccount extends BankAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	float rate = .05f;
	double maxWithLimit;
	private int age;
	private String gender;
	private String address;
	private String phoneNumber;

	public SavingsAccount(String name, double balance, double maxWithLimit, int age, String gender,
			String address, String phoneNumber) throws Exception {
		super(name, balance, 2000);
		this.maxWithLimit = maxWithLimit;
		this.age = age;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public double getNetBalance() {
		double NetBalance = getbalance() + (getbalance() * rate);
		return NetBalance;
	}

	public double getMaxWithLimit() {
		return maxWithLimit;
	}

	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void withdraw(double amount) throws MaxWithdraw, MaxBalance {
		if (amount < maxWithLimit) {
			super.withdraw(amount);
		} else {
			throw new MaxWithdraw("Maximum Withdraw Limit Exceed");
		}
	}

	@Override
	public String toString() {
		return "Name: " + getName() + ", Id: " + acc_num + ", Balance: " + getbalance() + ", Age: "
				+ age + ", Gender: " + gender + ", Address: " + address + ", Phone: " + phoneNumber
				+ ", Type: Savings Account";
	}
}
