package Data;

import Bank.Bank;

public class FileIO {
	public static Bank bank = null;

	public static void Read() {
		// Load bank data from PostgreSQL database
		FileIO.bank = DatabaseIO.loadBank();
	}

	public static void Write() {
		// Save bank data to PostgreSQL database
		DatabaseIO.saveBank(FileIO.bank);
	}
}
