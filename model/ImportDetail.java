package model;

import java.io.Serializable;

public class ImportDetail implements Serializable {
	private int id;
	private Material material;
	private int quantity;
	private float unitPrice;

	public ImportDetail() {
		super();
	}

	public ImportDetail(Material material, int quantity, float unitPrice) {
		super();
		this.material = material;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getTotal() {
		return quantity * unitPrice;
	}
}
