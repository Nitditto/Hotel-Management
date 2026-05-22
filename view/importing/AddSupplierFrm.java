package view.importing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dao.SupplierDAO;
import model.Supplier;

/**
 * JDialog popup for adding a new supplier.
 * Fields: Supplier Name (required), Address, Phone (required).
 */
public class AddSupplierFrm extends JDialog implements ActionListener {
	private JTextField txtName, txtAddress, txtTel;
	private JButton btnSave, btnCancel;
	private boolean saved = false;

	public AddSupplierFrm(JFrame parent) {
		super(parent, "Add New Supplier", true);

		JPanel pnMain = new JPanel(new BorderLayout(10, 10));
		pnMain.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		pnMain.setBackground(UIManager.getColor("Panel.background"));

		// ---- Form fields ----
		JPanel pnFields = new JPanel(new GridBagLayout());
		pnFields.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 5, 6, 5);
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblName = new JLabel("Supplier Name:");
		lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblName, gbc);

		txtName = new JTextField(22);
		txtName.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtName, gbc);

		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblAddress, gbc);

		txtAddress = new JTextField(22);
		txtAddress.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtAddress, gbc);

		JLabel lblTel = new JLabel("Phone:");
		lblTel.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblTel, gbc);

		txtTel = new JTextField(22);
		txtTel.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtTel, gbc);

		pnMain.add(pnFields, BorderLayout.CENTER);

		// ---- Buttons ----
		JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		pnButtons.setOpaque(false);
		btnSave = new JButton("Save");
		btnSave.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnSave.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnCancel.addActionListener(this);
		pnButtons.add(btnSave);
		pnButtons.add(btnCancel);
		pnMain.add(pnButtons, BorderLayout.SOUTH);

		this.setContentPane(pnMain);
		this.setSize(320, 280);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnCancel)) {
			this.dispose();
			return;
		}
		if (e.getSource().equals(btnSave)) {
			String name = txtName.getText().trim();
			String tel  = txtTel.getText().trim();

			// Validate required fields (Test case #14)
			if (name.isEmpty() || tel.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Supplier Name and Phone are required!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Supplier s = new Supplier();
			s.setName(name);
			s.setAddress(txtAddress.getText().trim());
			s.setTel(tel);
			s.setEmail("");

			if (new SupplierDAO().addSupplier(s)) {
				JOptionPane.showMessageDialog(this,
						"Supplier added successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				saved = true;
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Failed to add supplier!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
