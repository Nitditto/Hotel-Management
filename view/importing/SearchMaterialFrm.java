package view.importing;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import dao.MaterialDAO;
import model.ImportInvoice;
import model.Material;
import model.User;

public class SearchMaterialFrm extends JFrame implements ActionListener {
	private ArrayList<Material> listMaterial;
	private JTextField txtKey;
	private JButton btnSearch, btnAddNew, btnViewInvoice;
	private JTable tblResult;
	private DefaultTableModel tableModel;
	private User user;
	private ImportInvoice importInvoice;

	public SearchMaterialFrm(User user, ImportInvoice importInvoice) {
		super("Spa Management System");
		this.user = user;
		this.importInvoice = importInvoice;
		listMaterial = new ArrayList<>();

		JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
		mainPanel.setBackground(Color.WHITE);

		// ---- TOP: search bar ----
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 12));
		topPanel.setBackground(Color.WHITE);
		JLabel lblName = new JLabel("Material name/category:");
		lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
		topPanel.add(lblName);
		txtKey = new JTextField(25);
		txtKey.setFont(new Font("SansSerif", Font.PLAIN, 13));
		topPanel.add(txtKey);
		btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnSearch.addActionListener(this);
		topPanel.add(btnSearch);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// ---- CENTER: table ----
		String[] cols = {"ID", "Material name", "Category", "Current unit price", "Select"};
		tableModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return col == 4;
			}
		};

		tblResult = new JTable(tableModel);
		tblResult.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblResult.setRowHeight(28);
		tblResult.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblResult.setGridColor(new Color(200, 200, 200));
		tblResult.setShowGrid(true);

		tblResult.getColumnModel().getColumn(0).setPreferredWidth(80);
		tblResult.getColumnModel().getColumn(1).setPreferredWidth(250);
		tblResult.getColumnModel().getColumn(2).setPreferredWidth(150);
		tblResult.getColumnModel().getColumn(3).setPreferredWidth(120);
		tblResult.getColumnModel().getColumn(4).setPreferredWidth(100);

		tblResult.getColumnModel().getColumn(4).setCellRenderer(new SelectButtonRenderer());
		tblResult.getColumnModel().getColumn(4).setCellEditor(new SelectButtonEditor(this));

		JScrollPane scrollPane = new JScrollPane(tblResult);
		scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200)));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// ---- BOTTOM: action buttons ----
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
		bottomPanel.setBackground(new Color(245, 245, 245));
		bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
		
		btnAddNew = new JButton("Add new material");
		btnAddNew.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnAddNew.addActionListener(this);
		
		btnViewInvoice = new JButton("View Current Invoice");
		btnViewInvoice.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnViewInvoice.addActionListener(this);
		
		bottomPanel.add(btnAddNew);
		bottomPanel.add(btnViewInvoice);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Auto search on open to show all materials
		btnSearch.doClick();
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (int i = 0; i < listMaterial.size(); i++) {
			Material m = listMaterial.get(i);
			tableModel.addRow(new Object[]{
				m.getId(),
				m.getName(),
				m.getCategory(),
				m.getUnitPrice(),
				"Select"
			});
		}
	}

	void onSelectRow(int row) {
		if (row >= 0 && row < listMaterial.size()) {
			Material selected = listMaterial.get(row);
			(new ImportDetailFrm(user, importInvoice, selected)).setVisible(true);
			this.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSearch)) {
			String key = txtKey.getText().trim();
			listMaterial = new MaterialDAO().searchMaterial(key);
			refreshTable();

		} else if (e.getSource().equals(btnAddNew)) {
			AddMaterialFrm dialog = new AddMaterialFrm(this);
			dialog.setVisible(true);
			if (dialog.isSaved()) {
				btnSearch.doClick(); // reload
			}

		} else if (e.getSource().equals(btnViewInvoice)) {
			(new ImportDetailFrm(user, importInvoice, null)).setVisible(true);
			this.dispose();
		}
	}

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

	static class SelectButtonEditor extends DefaultCellEditor {
		private JButton button;
		private int clickedRow;
		private SearchMaterialFrm parent;

		public SelectButtonEditor(SearchMaterialFrm parent) {
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
