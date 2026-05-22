package test.unit;

import java.sql.Connection;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import dao.DAO;
import dao.MaterialDAO;
import model.Material;

public class MaterialDaoTest {
	MaterialDAO md = new MaterialDAO();

	/**
	 * Test case #12: Search existing material name
	 */
	@Test
	public void testSearchExistingMaterial() {
		String key = "Kem";
		ArrayList<Material> list = md.searchMaterial(key);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() > 0);
		for (int i = 0; i < list.size(); i++) {
			Assert.assertTrue(list.get(i).getName().toLowerCase().contains(key.toLowerCase()));
		}
	}

	/**
	 * Test case #13: Search non-existing material name
	 */
	@Test
	public void testSearchNonExistingMaterial() {
		String key = "zzzzzzzzz_nonexistent";
		ArrayList<Material> list = md.searchMaterial(key);
		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());
	}

	/**
	 * Test case #15: Add material with valid data (rollback after test)
	 */
	@Test
	public void testAddMaterialSuccess() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Material m = new Material("Test Material", "Test Category", 100000);
			boolean result = md.addMaterial(m);
			Assert.assertTrue(result);
			Assert.assertTrue(m.getId() > 0);

			// Verify by searching
			ArrayList<Material> list = md.searchMaterial("Test Material");
			Assert.assertTrue(list.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
