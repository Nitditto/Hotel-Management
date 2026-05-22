package model;

import java.io.Serializable;

public class ImportInvoiceDetail implements Serializable {
	private int id;
	private Material material;
	private float quantity;
	private float unitPrice;

	public ImportInvoiceDetail() {
		super();
	}

	public ImportInvoiceDetail(Material material, float quantity, float unitPrice) {
		super();
		this.material = material;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * Calculate total = quantity * unitPrice
	 * @return total amount for this detail line
	 */
	public float getTotal() {
		return quantity * unitPrice;
	}
}
