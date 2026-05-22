package test.unit;

import java.sql.Connection;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import dao.DAO;
import dao.SupplierDAO;
import model.Supplier;

public class SupplierDaoTest {
	SupplierDAO sd = new SupplierDAO();

	/**
	 * Test case #10: Search existing supplier name
	 */
	@Test
	public void testSearchExistingSupplier() {
		String key = "ABC";
		ArrayList<Supplier> list = sd.searchSupplier(key);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() > 0);
		for (int i = 0; i < list.size(); i++) {
			Assert.assertTrue(list.get(i).getName().toLowerCase().contains(key.toLowerCase()));
		}
	}

	/**
	 * Test case #11: Search non-existing supplier name
	 */
	@Test
	public void testSearchNonExistingSupplier() {
		String key = "zzzzzzzzz_nonexistent";
		ArrayList<Supplier> list = sd.searchSupplier(key);
		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());
	}

	/**
	 * Test case #14: Add supplier with valid data (rollback after test)
	 */
	@Test
	public void testAddSupplierSuccess() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = new Supplier("Test Supplier", "123 Test St", "0901111111", "test@test.com");
			boolean result = sd.addSupplier(s);
			Assert.assertTrue(result);
			Assert.assertTrue(s.getId() > 0);

			// Verify by searching
			ArrayList<Supplier> list = sd.searchSupplier("Test Supplier");
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
