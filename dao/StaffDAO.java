package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;

public class StaffDAO extends DAO {

	public StaffDAO() {
		super();
	}

	/**
	 * Check login credentials against tblStaff
	 * @param user User object with username and password set
	 * @return true if login is valid, also sets name, position, and id on the user object
	 */
	public boolean checkLogin(User user) {
		boolean result = false;
		String sql = "SELECT id, name, position FROM tblStaff WHERE username = ? AND password = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPosition(rs.getString("position"));
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
