package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Staff;

public class StaffDAO extends DAO {

	public StaffDAO() {
		super();
	}

	/**
	 * Check login credentials against tblStaff
	 * 
	 * @param user User object with username and password set
	 * @return true if login is valid, also sets name, role, and id on the user
	 *         object
	 */
	public boolean checkLogin(Staff user) {
		boolean result = false;
		String sql = "SELECT id, name, role FROM tblStaff WHERE username = ? AND password = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
