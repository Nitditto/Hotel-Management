package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ImportInvoice implements Serializable {
	private int id;
	private Supplier supplier;
	private Staff creator;
	private Date importDate;
	private String note;
	private ArrayList<ImportDetail> details = new ArrayList<>();

	public ImportInvoice() {
		super();
		this.details = new ArrayList<ImportDetail>();
	}

	public ImportInvoice(Supplier supplier, Staff creator, Date importDate, String note) {
		super();
		this.supplier = supplier;
		this.creator = creator;
		this.importDate = importDate;
		this.note = note;
		this.details = new ArrayList<ImportDetail>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Staff getCreator() {
		return creator;
	}

	public void setCreator(Staff creator) {
		this.creator = creator;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ArrayList<ImportDetail> getDetails() {
		return details;
	}

	public void setDetails(ArrayList<ImportDetail> details) {
		this.details = details;
	}

	/**
	 * Calculate total amount of the entire invoice
	 * 
	 * @return sum of all detail line totals
	 */
	public float getTotalAmount() {
		float total = 0;
		if (details != null) {
			for (ImportDetail d : details) {
				total += (d.getQuantity() * d.getUnitPrice());
			}
		}
		return total;
	}
}
