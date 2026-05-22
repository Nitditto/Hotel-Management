package view.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dao.StaffDAO;
import model.User;

public class LoginFrm extends JFrame implements ActionListener {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	public LoginFrm() {
		super("Spa Management System");

		JPanel pnMain = new JPanel(new GridBagLayout());
		pnMain.setBackground(new Color(235, 235, 235));

		JPanel pnForm = new JPanel(new GridBagLayout());
		pnForm.setBackground(new Color(235, 235, 235));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// Title
		JLabel lblTitle = new JLabel("Login");
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		pnForm.add(lblTitle, gbc);

		// Username label
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("SansSerif", Font.BOLD, 14));
		gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		pnForm.add(lblUsername, gbc);

		// Username field
		txtUsername = new JTextField(20);
		txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnForm.add(txtUsername, gbc);

		// Password label
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("SansSerif", Font.BOLD, 14));
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		pnForm.add(lblPassword, gbc);

		// Password field
		txtPassword = new JPasswordField(20);
		txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnForm.add(txtPassword, gbc);

		// Login button - blue
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnLogin.setBackground(new Color(70, 130, 220));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setOpaque(true);
		btnLogin.setBorderPainted(false);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setPreferredSize(new Dimension(130, 38));
		btnLogin.addActionListener(this);
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 10, 10, 10);
		pnForm.add(btnLogin, gbc);

		pnMain.add(pnForm);
		this.setContentPane(pnMain);
		this.setSize(500, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnLogin)) {
			User user = new User();
			user.setUsername(txtUsername.getText().trim());
			user.setPassword(new String(txtPassword.getPassword()));

			StaffDAO sd = new StaffDAO();
			if (sd.checkLogin(user)) {
				(new StaffHomeFrm(user)).setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Incorrect username and/or password!");
			}
		}
	}

	public static void main(String[] args) {
		LoginFrm myFrame = new LoginFrm();
		myFrame.setVisible(true);
	}
}
