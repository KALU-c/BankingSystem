package Bank;

public class StudentAccount extends SavingsAccount {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	String institutionName;

	public StudentAccount(String name, double balance, String institutionName) throws Exception {
		super(name, balance, 20000, 0, "", "", "");
		min_balance = 100;
		this.institutionName = institutionName;
		// this.type="Stuedent Account";
	}

	public String getInstitutionName() {
		return institutionName;
	}

}
