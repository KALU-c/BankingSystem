package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import Data.FileIO;

public class AddSavingsAccount extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Create the frame.
	 */
	public AddSavingsAccount() {
		setTitle("Add Savings Account ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAddCurrentAccount = new JLabel("Add Savings Account ");
		lblAddCurrentAccount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAddCurrentAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddCurrentAccount.setBounds(10, 11, 414, 34);
		contentPane.add(lblAddCurrentAccount);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblName.setBounds(10, 72, 124, 14);
		contentPane.add(lblName);

		textField = new JTextField();
		textField.setBounds(144, 69, 254, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblBalance.setBounds(10, 118, 124, 14);
		contentPane.add(lblBalance);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(144, 115, 254, 20);
		contentPane.add(textField_1);

		JLabel lblMaximumWithdrawLimit = new JLabel("Maximum Withdraw Limit:");
		lblMaximumWithdrawLimit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMaximumWithdrawLimit.setBounds(10, 163, 135, 14);
		contentPane.add(lblMaximumWithdrawLimit);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(144, 160, 254, 20);
		contentPane.add(textField_2);

		JLabel lblAge = new JLabel("Age:");
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAge.setBounds(10, 208, 124, 14);
		contentPane.add(lblAge);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(144, 205, 254, 20);
		contentPane.add(textField_3);

		JLabel lblGender = new JLabel("Gender:");
		lblGender.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblGender.setBounds(10, 253, 124, 14);
		contentPane.add(lblGender);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(144, 250, 254, 20);
		contentPane.add(textField_4);

		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAddress.setBounds(10, 298, 124, 14);
		contentPane.add(lblAddress);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(144, 295, 254, 20);
		contentPane.add(textField_5);

		JLabel lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPhoneNumber.setBounds(10, 343, 124, 14);
		contentPane.add(lblPhoneNumber);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(144, 340, 254, 20);
		contentPane.add(textField_6);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				double bal = Double.parseDouble(textField_1.getText());
				double maxw = Double.parseDouble(textField_2.getText());
				int age = Integer.parseInt(textField_3.getText());
				String gender = textField_4.getText();
				String address = textField_5.getText();
				String phoneNumber = textField_6.getText();

				if (bal < 2000) {
					JOptionPane.showMessageDialog(getComponent(0), "Minimum Limit 2000", "Warning", 0);
					textField.setText(null);
					textField_1.setText(null);
					textField_2.setText(null);
					textField_3.setText(null);
					textField_4.setText(null);
					textField_5.setText(null);
					textField_6.setText(null);
				} else {
					if (name == null || bal <= 0 || maxw <= 0 || age <= 0 || gender.isEmpty()
							|| address.isEmpty() || phoneNumber.isEmpty()) {
						JOptionPane.showMessageDialog(getComponent(0), "Please fill all fields correctly!");
						textField.setText(null);
						textField_1.setText(null);
						textField_2.setText(null);
						textField_3.setText(null);
						textField_4.setText(null);
						textField_5.setText(null);
						textField_6.setText(null);
					} else {
						int ch = JOptionPane.showConfirmDialog(getComponent(0), "Confirm?");
						if (ch == 0) {
							try {
								int index =
										FileIO.bank.addAccount(name, bal, maxw, age, gender, address, phoneNumber);
								DisplayList.arr.addElement(FileIO.bank.getAccounts()[index].toString());
								FileIO.Write();
								JOptionPane.showMessageDialog(getComponent(0), "Added Successfully");
								dispose();
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(getComponent(0), "Error: " + ex.getMessage(), "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(getComponent(0), "Failed");
						}
						textField.setText(null);
						textField_1.setText(null);
						textField_2.setText(null);
						textField_3.setText(null);
						textField_4.setText(null);
						textField_5.setText(null);
						textField_6.setText(null);
					}
				}
			}
		});

		btnAdd.setBounds(86, 409, 89, 23);
		contentPane.add(btnAdd);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(null);
				textField_1.setText(null);
				textField_2.setText(null);
				textField_3.setText(null);
				textField_4.setText(null);
				textField_5.setText(null);
				textField_6.setText(null);
			}
		});
		btnReset.setBounds(309, 409, 89, 23);
		contentPane.add(btnReset);
	}
}
