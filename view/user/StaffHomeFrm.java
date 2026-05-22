package view.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Staff;
import view.importing.SearchSupplierFrm;

public class StaffHomeFrm extends JFrame implements ActionListener {
	private JButton btnImport, btnLogout;
	private Staff Staff;

	public StaffHomeFrm(Staff Staff) {
		super("Spa Management System");
		this.Staff = Staff;

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(235, 235, 235));

		// ---- TOP: Staff info + logout button ----
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		topPanel.setBackground(new Color(235, 235, 235));
		JLabel lblUser = new JLabel("name: " + Staff.getName());
		lblUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btnLogout.addActionListener(this);
		topPanel.add(lblUser);
		topPanel.add(btnLogout);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// ---- CENTER: title + import button ----
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(new Color(235, 235, 235));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 30, 20);
		gbc.anchor = GridBagConstraints.CENTER;

		// Title
		JLabel lblTitle = new JLabel("InventoryStaffHome");
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
		gbc.gridx = 0;
		gbc.gridy = 0;
		centerPanel.add(lblTitle, gbc);

		// Import Materials big button
		btnImport = new JButton("Import Materials");
		btnImport.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnImport.setPreferredSize(new Dimension(280, 65));
		btnImport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImport.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 1;
		centerPanel.add(btnImport, gbc);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		this.setContentPane(mainPanel);
		this.setSize(800, 550);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnImport)) {
			(new SearchSupplierFrm(Staff)).setVisible(true);
			this.dispose();
		} else if (e.getSource().equals(btnLogout)) {
			(new LoginFrm()).setVisible(true);
			this.dispose();
		}
	}
}
