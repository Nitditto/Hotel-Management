package view.importing;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.ImportInvoiceDAO;
import model.ImportInvoice;
import model.ImportInvoiceDetail;
import model.Material;
import model.User;

public class ImportDetailFrm extends JFrame implements ActionListener {
	private User user;
	private ImportInvoice importInvoice;
	private Material selectedMaterial;
	
	private JTextField txtQuantity, txtPrice;
	private JButton btnAdd, btnBack, btnSubmit;
	private JTable tblDetails;
	private DefaultTableModel tableModel;
	private JLabel lblTotal;

	public ImportDetailFrm(User user, ImportInvoice importInvoice, Material selectedMaterial) {
		super("Spa Management System");
		this.user = user;
		this.importInvoice = importInvoice;
		this.selectedMaterial = selectedMaterial;

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 240, 240));

		// ==========================================
		// TOP PANEL (Split into Left and Right)
		// ==========================================
		JPanel topPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		topPanel.setBackground(new Color(240, 240, 240));

		// --- Left: Invoice Information ---
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
		pnInfo.setBackground(new Color(245, 245, 245));
		pnInfo.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)), 
				"Invoice Information", TitledBorder.LEFT, TitledBorder.TOP, 
				new Font("SansSerif", Font.BOLD, 12)));
		
		pnInfo.add(Box.createRigidArea(new Dimension(0, 15)));
		JLabel lblSupName = new JLabel("- Supplier Name: " + importInvoice.getSupplier().getName());
		lblSupName.setFont(new Font("SansSerif", Font.BOLD, 12));
		pnInfo.add(lblSupName);
		pnInfo.add(Box.createRigidArea(new Dimension(0, 25)));
		
		JLabel lblSupAddress = new JLabel("- Address: " + importInvoice.getSupplier().getAddress());
		lblSupAddress.setFont(new Font("SansSerif", Font.BOLD, 12));
		pnInfo.add(lblSupAddress);
		pnInfo.add(Box.createRigidArea(new Dimension(0, 25)));
		
		JLabel lblSupPhone = new JLabel("- Phone: " + importInvoice.getSupplier().getTel());
		lblSupPhone.setFont(new Font("SansSerif", Font.BOLD, 12));
		pnInfo.add(lblSupPhone);
		pnInfo.add(Box.createRigidArea(new Dimension(0, 15)));
		
		topPanel.add(pnInfo);

		// --- Right: Material Detail Input ---
		JPanel pnInput = new JPanel(new GridBagLayout());
		pnInput.setBackground(new Color(245, 245, 245));
		pnInput.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)), 
				"Material Detail Input", TitledBorder.LEFT, TitledBorder.TOP, 
				new Font("SansSerif", Font.BOLD, 12)));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.EAST;
		
		JLabel lblSelected = new JLabel("Selected Material: " + 
				(selectedMaterial != null ? selectedMaterial.getName() : "None"));
		lblSelected.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblSelected.setForeground(Color.BLUE);
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		pnInput.add(lblSelected, gbc);
		
		gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
		JLabel lblQty = new JLabel("Quantity:");
		lblQty.setFont(new Font("SansSerif", Font.BOLD, 12));
		gbc.gridx = 0; gbc.gridy = 1;
		pnInput.add(lblQty, gbc);
		
		txtQuantity = new JTextField(15);
		gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
		pnInput.add(txtQuantity, gbc);
		
		JLabel lblPrice = new JLabel("Import Unit Price:");
		lblPrice.setFont(new Font("SansSerif", Font.BOLD, 12));
		gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
		pnInput.add(lblPrice, gbc);
		
		txtPrice = new JTextField(15);
		if (selectedMaterial != null) {
			txtPrice.setText(String.valueOf(selectedMaterial.getUnitPrice()));
		}
		gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
		pnInput.add(txtPrice, gbc);
		
		btnAdd = new JButton("Add to invoice");
		btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAdd.addActionListener(this);
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		pnInput.add(btnAdd, gbc);

		topPanel.add(pnInput);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// ==========================================
		// CENTER PANEL (Table)
		// ==========================================
		String[] cols = {"No", "Material name", "Quantity", "Import unit price", "Subtotal"};
		tableModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) { return false; }
		};
		tblDetails = new JTable(tableModel);
		tblDetails.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tblDetails.setRowHeight(25);
		
		tblDetails.getColumnModel().getColumn(0).setPreferredWidth(50);
		tblDetails.getColumnModel().getColumn(1).setPreferredWidth(250);
		tblDetails.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblDetails.getColumnModel().getColumn(3).setPreferredWidth(150);
		tblDetails.getColumnModel().getColumn(4).setPreferredWidth(150);
		
		JScrollPane scrollPane = new JScrollPane(tblDetails);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// ==========================================
		// BOTTOM PANEL (Total + Buttons)
		// ==========================================
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(245, 245, 245));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		lblTotal = new JLabel("- Total Amount : 0.0");
		lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
		bottomPanel.add(lblTotal, BorderLayout.WEST);
		
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		btnPanel.setOpaque(false);
		
		btnBack = new JButton("Back to Search Material");
		btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnBack.addActionListener(this);
		
		btnSubmit = new JButton("Submit Invoice");
		btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnSubmit.addActionListener(this);
		
		btnPanel.add(btnBack);
		btnPanel.add(btnSubmit);
		bottomPanel.add(btnPanel, BorderLayout.EAST);
		
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setSize(900, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		refreshTable();
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		ArrayList<ImportInvoiceDetail> details = importInvoice.getDetails();
		for (int i = 0; i < details.size(); i++) {
			ImportInvoiceDetail d = details.get(i);
			tableModel.addRow(new Object[]{
				i + 1,
				d.getMaterial().getName(),
				d.getQuantity(),
				String.format("%.1f", d.getUnitPrice()),
				String.format("%.1f", d.getTotal())
			});
		}
		lblTotal.setText("- Total Amount : " + String.format("%.1f", importInvoice.getTotalAmount()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAdd)) {
			if (selectedMaterial == null) {
				JOptionPane.showMessageDialog(this,
						"No material selected! Please go back and select a material first.",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String qtyStr = txtQuantity.getText().trim();
			String priceStr = txtPrice.getText().trim();
			
			if (qtyStr.isEmpty() || priceStr.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Quantity and Unit Price cannot be empty!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				float qty = Float.parseFloat(qtyStr);
				float price = Float.parseFloat(priceStr);
				
				if (qty <= 0 || price <= 0) {
					JOptionPane.showMessageDialog(this,
							"Quantity and Unit Price must be > 0!",
							"Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ImportInvoiceDetail detail = new ImportInvoiceDetail(selectedMaterial, qty, price);
				importInvoice.getDetails().add(detail);
				refreshTable();
				
				// Reset inputs
				selectedMaterial = null;
				txtQuantity.setText("");
				txtPrice.setText("");
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Quantity and Unit Price must be valid numbers!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource().equals(btnBack)) {
			(new SearchMaterialFrm(user, importInvoice)).setVisible(true);
			this.dispose();

		} else if (e.getSource().equals(btnSubmit)) {
			if (importInvoice.getDetails().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Cannot submit an empty invoice! Please add at least one material.",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ImportInvoiceDAO dao = new ImportInvoiceDAO();
			if (dao.addImportInvoice(importInvoice)) {
				printInvoiceConsole(importInvoice);
				JOptionPane.showMessageDialog(this,
						"Successful import message! Saved data and printed invoice.",
						"Message", JOptionPane.INFORMATION_MESSAGE);
				
				(new view.user.StaffHomeFrm(user)).setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Failed to save invoice!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void printInvoiceConsole(ImportInvoice inv) {
		System.out.println("=== PRINT INVOICE ===");
		System.out.println("ID: " + inv.getId());
		System.out.println("Supplier: " + inv.getSupplier().getName());
		System.out.println("Total: " + String.format("%.1f", inv.getTotalAmount()));
		System.out.println("=====================");
	}
}
