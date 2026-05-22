package view.importing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dao.MaterialDAO;
import model.Material;

public class AddMaterialFrm extends JDialog implements ActionListener {
	private JTextField txtName, txtCategory, txtUnitPrice;
	private JButton btnSave, btnCancel;
	private boolean saved = false;

	public AddMaterialFrm(JFrame parent) {
		super(parent, "Add New Material", true);

		JPanel pnMain = new JPanel(new BorderLayout(10, 10));
		pnMain.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		pnMain.setBackground(new Color(235, 235, 235));

		// ---- Form fields ----
		JPanel pnFields = new JPanel(new GridBagLayout());
		pnFields.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 5, 6, 5);
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblName = new JLabel("Material Name:");
		lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblName, gbc);

		txtName = new JTextField(22);
		txtName.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtName, gbc);

		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblCategory, gbc);

		txtCategory = new JTextField(22);
		txtCategory.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtCategory, gbc);

		JLabel lblUnitPrice = new JLabel("Unit Price:");
		lblUnitPrice.setFont(new Font("SansSerif", Font.BOLD, 13));
		gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
		pnFields.add(lblUnitPrice, gbc);

		txtUnitPrice = new JTextField(22);
		txtUnitPrice.setFont(new Font("SansSerif", Font.PLAIN, 13));
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
		pnFields.add(txtUnitPrice, gbc);

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
			String priceStr = txtUnitPrice.getText().trim();

			if (name.isEmpty() || priceStr.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Material Name and Unit Price are required!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			float price = 0;
			try {
				price = Float.parseFloat(priceStr);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Unit Price must be a number!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Material m = new Material();
			m.setName(name);
			m.setCategory(txtCategory.getText().trim());
			m.setUnitPrice(price);

			if (new MaterialDAO().addMaterial(m)) {
				JOptionPane.showMessageDialog(this,
						"Material added successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				saved = true;
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Failed to add material!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
