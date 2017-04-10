
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
/************************************************************
 * This app is a GUI that lets the user pull up
 * a list of names, email addresses and phone numbers
 * in an address book. The list is stored in a SQL database.
 * The user can add, update and delete the list.
 * 
 * @authors Jennifer Bunte and Mary A Stewart
 * @version 2.0.0  
 *************************************************************/

public class AddressBookFrame extends JFrame {
	
	//Variables
	private JTextArea addressbook;
	private JPanel contentPane;
	private JTextField nameTFld;
	private JTextField emailTFld;
	private JTextField phoneTFld;
	private JScrollPane scrollPane_1;
	PrintWriter out;
	Statement statement = null;
	Connection connection = null;
	int count = 0;
	//new object
	SwingValidator sv = new SwingValidator();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddressBookFrame frame = new AddressBookFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddressBookFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 599, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Name:");

		JLabel lblNewLabel_1 = new JLabel("Email:");
		//text fields
		nameTFld = new JTextField();
		nameTFld.setColumns(10);
		nameTFld.requestFocusInWindow();

		emailTFld = new JTextField();
		emailTFld.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Phone:");

		phoneTFld = new JTextField();
		phoneTFld.setColumns(10);

		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				clearTextField();
			}
		});

		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isValidData()) {

					getStatementConnection();

					String insertStatement = "INSERT INTO AddressBook (Name, Email, PhoneNumber) " + "VALUES (?,?,?)";
					try {
						PreparedStatement ps = connection.prepareStatement(insertStatement);
						ps.setString(1, nameTFld.getText());
						ps.setString(2, emailTFld.getText());
						ps.setString(3, phoneTFld.getText());

						if (checkForDuplicate(getConnection()))
							count = ps.executeUpdate();
					} catch (SQLException e1) {

						e1.printStackTrace();
					}

					clearTextField();
					fillAddressBook();
					nameTFld.requestFocusInWindow();

					if (count > 0) {
						sv.showPositiveMessage(addressbook, "Entry successfully added to the address book.");
					} else {
						sv.showNegativeMessage(addressbook, "Error! Duplicate information. Information not added.");
						clearTextField();
					}
				}
			}
		});

		JButton exitBtn = new JButton("Exit");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JButton listBtn = new JButton("List");
		listBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				fillAddressBook();
				nameTFld.requestFocusInWindow();
			}
		});

		scrollPane_1 = new JScrollPane();

		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					getStatementConnection();
					String deleteStatement = "DELETE FROM AddressBook " + "WHERE Name = ? ";
					try {
						PreparedStatement ps = connection.prepareStatement(deleteStatement);
						ps.setString(1, nameTFld.getText());

						count = ps.executeUpdate();
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
					clearTextField();
					fillAddressBook();
					nameTFld.requestFocusInWindow();

					if (count > 0) {
						sv.showPositiveMessage(addressbook,
								"An existing entry in the address book was deleted successfully.");
					} else if (count <= 0) {
						sv.showNegativeMessage(addressbook,
								"There is no matching name in the address book. Please try again.");

					}
				}
		});

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {

					getStatementConnection();

					String updateEntry = "UPDATE AddressBook " + "SET Email = ?, PhoneNumber = ? " + "WHERE Name = ?";
					try {
						PreparedStatement ps = connection.prepareStatement(updateEntry);
						ps.setString(1, emailTFld.getText());
						ps.setString(2, phoneTFld.getText());
						ps.setString(3, nameTFld.getText());
						count = ps.executeUpdate();

					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
					clearTextField();
					fillAddressBook();
					nameTFld.requestFocusInWindow();

					if (count > 0) {
						sv.showPositiveMessage(addressbook,
								"An existing entry in the address book was updated sucessfully.");
					} else if (count <= 0) {
						sv.showNegativeMessage(addressbook,
								"There is no matching name in the address book. Please try again.");

					}
				}

			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panel.createSequentialGroup()
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
												.createSequentialGroup()
												.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
														.addComponent(lblNewLabel)
														.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
																.addComponent(lblNewLabel_2)
																.addComponent(lblNewLabel_1)))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(emailTFld, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(nameTFld, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(phoneTFld, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGap(62).addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 263,
														GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panel.createSequentialGroup().addComponent(addBtn)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(clearBtn).addGap(18).addComponent(deleteBtn)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(updateBtn).addGap(27).addComponent(listBtn)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(exitBtn)))
										.addGap(145)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(31)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel)
										.addComponent(nameTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGap(29)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1)
										.addComponent(emailTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGap(41)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_2)
										.addComponent(phoneTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 281,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(deleteBtn)
										.addComponent(clearBtn).addComponent(addBtn).addComponent(exitBtn)
										.addComponent(listBtn).addComponent(updateBtn))))
				.addGap(164))

		);

		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollPane_1.setColumnHeaderView(scrollBar);

		addressbook = new JTextArea();
		addressbook.setFocusable(false);//skips default focus from left to right or TFldname to addressbook
		scrollPane_1.setViewportView(addressbook);
		panel.setLayout(gl_panel);
	}

					//METHODS	
	private Connection getConnection() throws SQLException {		

		// set up the home directory for Derby
		String dbDirectory = "c:/murach/java/db";
		System.setProperty("derby.system.home", dbDirectory);

		// create and return the connection
		String dbUrl = "jdbc:derby:AddressBookDB";
		connection = DriverManager.getConnection(dbUrl);

		return connection;
	}
	
	private void fillList(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			String query = "SELECT Name, Email, PhoneNumber FROM AddressBook";
			ResultSet rs = statement.executeQuery(query);
			String output = "";
			while (rs.next()) {
				String entry = rs.getString("Name") + "\t" + rs.getString("Email") + "\t" + rs.getString("PhoneNumber")
						+ " ";
				// out.print(entry);
				output += entry + "\n";
			}
			addressbook.setText(output);
			// out.close();
			rs.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				e.printStackTrace();
			}
		}
	}

	public void clearTextField() {
		nameTFld.setText("");
		emailTFld.setText("");
		phoneTFld.setText("");
	}

	public void getStatementConnection() {

		try {
			statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	private boolean isValidData() {		
		    return sv.isPresent(nameTFld, "Name") && sv.isValidName(nameTFld, "Name")
				&& sv.isPresent(emailTFld, "Email")	&& sv.isValidEmail(emailTFld, "Email")
				&& sv.isPresent(phoneTFld, "Phone Number") && sv.isValidPhoneNumber(phoneTFld, "Phone Number");
	}

	public void fillAddressBook() {
		try {
			fillList(getConnection());
			addressbook.setCaretPosition(0);
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}

	private boolean checkForDuplicate(Connection connection) {
		try {
			statement = connection.createStatement();
			String query = "SELECT * FROM AddressBook";
			ResultSet rs = statement.executeQuery(query);
			int dupeName = 0;
			int dupeEmail = 0;
			int dupePhone = 0;
			while (rs.next()) {
				if (nameTFld.getText().equals(rs.getString("Name"))) {
					nameTFld.setText("REPLACE");
					dupeName++;
				}

				if (emailTFld.getText().equals(rs.getString("Email"))) {
					emailTFld.setText("REPLACE");
					dupeEmail++;
				}

				if (phoneTFld.getText().equals(rs.getString("PhoneNumber"))) {
					phoneTFld.setText("REPLACE");
					dupePhone++;
				}
			}

			if (dupeName > 0 || dupeEmail > 0 || dupePhone > 0) {
				showDuplicateErrorMsg(dupeName, dupeEmail, dupePhone);
				return false;
			}

			rs.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private void showDuplicateErrorMsg(int dupeName, int dupeEmail, int dupePhone) {
		if (dupeName > 0 && dupeEmail > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name, email, and phone entries");
		else if (dupeName > 0 && dupeEmail > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name and email entries");
		else if (dupeName > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name and phone entries");
		else if (dupeEmail > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate email and phone entries");
		else if (dupeName > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name entry");
		else if (dupeEmail > 0)
			sv.showNegativeMessage(addressbook, "Duplicate email entry");
		else if (dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate phone entry");
	}

}
