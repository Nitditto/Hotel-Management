package view.importing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import dao.SupplierDAO;
import model.*;

/**
 * Screen 1: Search and select a supplier.
 * Table has a "Select" button column. Clicking Select creates ImportInvoice
 * and navigates to SearchMaterialFrm.
 */
public class SearchSupplierFrm extends JFrame implements ActionListener {
	private ArrayList<Supplier> listSupplier;
	private JTextField txtKey;
	private JButton btnSearch, btnAddNew, btnCancel;
	private JTable tblResult;
	private DefaultTableModel tableModel;
	private Staff user;

	public SearchSupplierFrm(Staff user) {
		super("Spa Management System");
		this.user = user;
		listSupplier = new ArrayList<>();

		JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
		mainPanel.setBackground(Color.WHITE);

		// ---- TOP: search bar ----
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 12));
		topPanel.setBackground(Color.WHITE);
		JLabel lblName = new JLabel("Supplier name:");
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 13));
		topPanel.add(lblName);
		txtKey = new JTextField(25);
		txtKey.setFont(new Font("SansSerif", Font.PLAIN, 13));
		topPanel.add(txtKey);
		btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnSearch.addActionListener(this);
		topPanel.add(btnSearch);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// ---- CENTER: table ----
		String[] cols = {"No", "ID", "Supplier name", "Address", "Phone", "Select"};
		tableModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return col == 5;
			}
		};

		tblResult = new JTable(tableModel);
		tblResult.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblResult.setRowHeight(28);
		tblResult.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblResult.setGridColor(new Color(200, 200, 200));
		tblResult.setShowGrid(true);

		// Column widths
		tblResult.getColumnModel().getColumn(0).setPreferredWidth(60);
		tblResult.getColumnModel().getColumn(1).setPreferredWidth(60);
		tblResult.getColumnModel().getColumn(2).setPreferredWidth(200);
		tblResult.getColumnModel().getColumn(3).setPreferredWidth(200);
		tblResult.getColumnModel().getColumn(4).setPreferredWidth(120);
		tblResult.getColumnModel().getColumn(5).setPreferredWidth(100);

		// Set Select button column renderer and editor
		tblResult.getColumnModel().getColumn(5).setCellRenderer(new SelectButtonRenderer());
		tblResult.getColumnModel().getColumn(5).setCellEditor(new SelectButtonEditor(this));

		JScrollPane scrollPane = new JScrollPane(tblResult);
		scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200)));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// ---- BOTTOM: action buttons ----
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
		btnAddNew = new JButton("Add new supplier");
		btnAddNew.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnAddNew.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btnCancel.addActionListener(this);
		bottomPanel.add(btnAddNew);
		bottomPanel.add(btnCancel);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setSize(820, 620);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/** Refresh table data from listSupplier */
	private void refreshTable() {
		tableModel.setRowCount(0);
		for (int i = 0; i < listSupplier.size(); i++) {
			Supplier s = listSupplier.get(i);
			tableModel.addRow(new Object[]{
				i + 1,
				s.getId(),
				s.getName(),
				s.getAddress(),
				s.getTel(),
				"Select"
			});
		}
	}

	/** Called by SelectButtonEditor when user clicks Select on a row */
	void onSelectRow(int row) {
		if (row >= 0 && row < listSupplier.size()) {
			Supplier selected = listSupplier.get(row);
			ImportInvoice inv = new ImportInvoice();
			inv.setSupplier(selected);
			inv.setCreator(user);
			inv.setImportDate(new java.util.Date());
			(new SearchMaterialFrm(user, inv)).setVisible(true);
			this.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSearch)) {
			String key = txtKey.getText().trim();
			if (key.isEmpty()) return;
			listSupplier = new SupplierDAO().searchSupplier(key);
			refreshTable();

		} else if (e.getSource().equals(btnAddNew)) {
			AddSupplierFrm dialog = new AddSupplierFrm(this);
			dialog.setVisible(true);
			if (dialog.isSaved() && !txtKey.getText().trim().isEmpty()) {
				listSupplier = new SupplierDAO().searchSupplier(txtKey.getText().trim());
				refreshTable();
			}

		} else if (e.getSource().equals(btnCancel)) {
			(new view.user.StaffHomeFrm(user)).setVisible(true);
			this.dispose();
		}
	}

	// =========================================================
	// Inner class: renderer for Select button column
	// =========================================================
	static class SelectButtonRenderer extends JButton implements TableCellRenderer {
		public SelectButtonRenderer() {
			setOpaque(true);
			setText("Select");
			setFont(new Font("SansSerif", Font.BOLD, 12));
		}

		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			setBackground(new Color(210, 225, 245));
			return this;
		}
	}

	// =========================================================
	// Inner class: editor for Select button column
	// =========================================================
	static class SelectButtonEditor extends DefaultCellEditor {
		private JButton button;
		private int clickedRow;
		private SearchSupplierFrm parent;

		public SelectButtonEditor(SearchSupplierFrm parent) {
			super(new JCheckBox());
			this.parent = parent;
			setClickCountToStart(1);

			button = new JButton("Select");
			button.setFont(new Font("SansSerif", Font.BOLD, 12));
			button.setBackground(new Color(210, 225, 245));
			button.setOpaque(true);
			button.addActionListener(e -> {
				fireEditingStopped();
				parent.onSelectRow(clickedRow);
			});
		}

		@Override
		public Component getTableCellEditorComponent(
				JTable table, Object value, boolean isSelected, int row, int col) {
			clickedRow = row;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return "Select";
		}
	}
}
