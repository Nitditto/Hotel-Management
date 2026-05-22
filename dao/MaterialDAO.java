package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import model.Material;

public class MaterialDAO extends DAO {

	public MaterialDAO() {
		super();
	}

	/**
	 * Search materials whose name or category contains the keyword
	 * @param key the search keyword
	 * @return list of materials whose name or category contains the keyword
	 */
	public ArrayList<Material> searchMaterial(String key) {
		ArrayList<Material> result = new ArrayList<Material>();
		String sql = "SELECT * FROM tblMaterial WHERE name LIKE ? OR category LIKE ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + key + "%");
			ps.setString(2, "%" + key + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Material m = new Material();
				m.setId(rs.getInt("id"));
				m.setName(rs.getString("name"));
				m.setCategory(rs.getString("category"));
				m.setUnitPrice(rs.getFloat("unitPrice"));
				result.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Add a new material to the database
	 * @param m the material to add
	 * @return true if the material was added successfully
	 */
	public boolean addMaterial(Material m) {
		String sql = "INSERT INTO tblMaterial(name, category, unitPrice) VALUES(?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getName());
			ps.setString(2, m.getCategory());
			ps.setFloat(3, m.getUnitPrice());

			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				m.setId(generatedKeys.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
