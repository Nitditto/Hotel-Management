package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import model.Supplier;

public class SupplierDAO extends DAO {

	public SupplierDAO() {
		super();
	}

	/**
	 * Search suppliers whose name contains the keyword
	 * @param key the search keyword
	 * @return list of suppliers whose name contains the keyword
	 */
	public ArrayList<Supplier> searchSupplier(String key) {
		ArrayList<Supplier> result = new ArrayList<Supplier>();
		String sql = "SELECT * FROM tblSupplier WHERE name LIKE ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + key + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Supplier s = new Supplier();
				s.setId(rs.getInt("id"));
				s.setName(rs.getString("name"));
				s.setAddress(rs.getString("address"));
				s.setTel(rs.getString("tel"));
				s.setEmail(rs.getString("email"));
				result.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Add a new supplier to the database
	 * @param s the supplier to add
	 * @return true if the supplier was added successfully
	 */
	public boolean addSupplier(Supplier s) {
		String sql = "INSERT INTO tblSupplier(name, address, tel, email) VALUES(?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, s.getName());
			ps.setString(2, s.getAddress());
			ps.setString(3, s.getTel());
			ps.setString(4, s.getEmail());

			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				s.setId(generatedKeys.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
