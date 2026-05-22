package model;

import java.io.Serializable;

public class Supplier implements Serializable {
	private int id;
	private String name;
	private String address;
	private String tel;
	private String email;

	public Supplier() {
		super();
	}

	public Supplier(String name, String address, String tel, String email) {
		super();
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
