package test.unit;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import dao.DAO;
import dao.ImportInvoiceDAO;
import dao.MaterialDAO;
import dao.SupplierDAO;
import model.ImportInvoice;
import model.ImportInvoiceDetail;
import model.Material;
import model.Supplier;
import model.User;

public class ImportInvoiceDaoTest {
	ImportInvoiceDAO invoiceDAO = new ImportInvoiceDAO();
	SupplierDAO supplierDAO = new SupplierDAO();
	MaterialDAO materialDAO = new MaterialDAO();

	/**
	 * Helper: get an existing supplier from sample data
	 */
	private Supplier getExistingSupplier() {
		ArrayList<Supplier> list = supplierDAO.searchSupplier("ABC");
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * Helper: get an existing material from sample data
	 */
	private Material getExistingMaterial(String key) {
		ArrayList<Material> list = materialDAO.searchMaterial(key);
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * Helper: create a dummy user (staff) for testing
	 */
	private User getDummyUser() {
		User u = new User();
		u.setId(1);
		u.setName("Test Staff");
		return u;
	}

	/**
	 * Test case #1: Supplier exists, Material exists
	 */
	@Test
	public void testAddInvoice_ExistingSupplier_ExistingMaterial() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = getExistingSupplier();
			Assert.assertNotNull("Sample supplier must exist", s);
			Material m = getExistingMaterial("Kem");
			Assert.assertNotNull("Sample material must exist", m);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #1");
			inv.getDetails().add(new ImportInvoiceDetail(m, 10, m.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertTrue(inv.getId() > 0);
			Assert.assertTrue(inv.getDetails().get(0).getId() > 0);
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

	/**
	 * Test case #2: Supplier does not exist (add new), Material exists
	 */
	@Test
	public void testAddInvoice_NewSupplier_ExistingMaterial() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = new Supplier("New Supplier Test", "Address", "0900000000", "new@test.com");
			supplierDAO.addSupplier(s);
			Assert.assertTrue(s.getId() > 0);

			Material m = getExistingMaterial("Kem");
			Assert.assertNotNull(m);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #2");
			inv.getDetails().add(new ImportInvoiceDetail(m, 5, m.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertTrue(inv.getId() > 0);
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

	/**
	 * Test case #3: Supplier exists, Material does not exist (add new)
	 */
	@Test
	public void testAddInvoice_ExistingSupplier_NewMaterial() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = getExistingSupplier();
			Assert.assertNotNull(s);

			Material m = new Material("New Test Material", "Test Category", 99000);
			materialDAO.addMaterial(m);
			Assert.assertTrue(m.getId() > 0);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #3");
			inv.getDetails().add(new ImportInvoiceDetail(m, 3, m.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertTrue(inv.getId() > 0);
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

	/**
	 * Test case #4: Supplier does not exist, Material does not exist (both new)
	 */
	@Test
	public void testAddInvoice_NewSupplier_NewMaterial() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = new Supplier("Brand New Supplier", "Addr", "0911111111", "bn@test.com");
			supplierDAO.addSupplier(s);

			Material m = new Material("Brand New Material", "New Cat", 50000);
			materialDAO.addMaterial(m);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #4");
			inv.getDetails().add(new ImportInvoiceDetail(m, 7, m.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertTrue(inv.getId() > 0);
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

	/**
	 * Test case #5: Supplier exists, add 2 duplicate materials
	 */
	@Test
	public void testAddInvoice_DuplicateMaterials() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = getExistingSupplier();
			Material m = getExistingMaterial("Kem");
			Assert.assertNotNull(s);
			Assert.assertNotNull(m);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #5");
			inv.getDetails().add(new ImportInvoiceDetail(m, 5, m.getUnitPrice()));
			inv.getDetails().add(new ImportInvoiceDetail(m, 10, m.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertEquals(2, inv.getDetails().size());
			Assert.assertTrue(inv.getTotalAmount() > 0);
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

	/**
	 * Test case #6: Supplier exists, add 2 different materials
	 */
	@Test
	public void testAddInvoice_TwoDifferentMaterials() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = getExistingSupplier();
			Material m1 = getExistingMaterial("Kem");
			Material m2 = getExistingMaterial("Dầu");
			Assert.assertNotNull(s);
			Assert.assertNotNull(m1);
			Assert.assertNotNull(m2);

			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #6");
			inv.getDetails().add(new ImportInvoiceDetail(m1, 5, m1.getUnitPrice()));
			inv.getDetails().add(new ImportInvoiceDetail(m2, 3, m2.getUnitPrice()));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);
			Assert.assertEquals(2, inv.getDetails().size());

			float expectedTotal = (5 * m1.getUnitPrice()) + (3 * m2.getUnitPrice());
			Assert.assertEquals(expectedTotal, inv.getTotalAmount(), 0.01f);
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

	/**
	 * Test case #7: Edit imported unit price before submit
	 */
	@Test
	public void testAddInvoice_EditUnitPrice() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);

			Supplier s = getExistingSupplier();
			Material m = getExistingMaterial("Kem");
			Assert.assertNotNull(s);
			Assert.assertNotNull(m);

			float customPrice = 999999;
			ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #7");
			inv.getDetails().add(new ImportInvoiceDetail(m, 2, customPrice));

			boolean result = invoiceDAO.addImportInvoice(inv);
			Assert.assertTrue(result);

			float expectedTotal = 2 * customPrice;
			Assert.assertEquals(expectedTotal, inv.getTotalAmount(), 0.01f);
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

	/**
	 * Test case #8: Submit invoice without any material (empty details)
	 * The DAO should still succeed (business validation is in View layer),
	 * but we test that getTotalAmount() == 0.
	 */
	@Test
	public void testAddInvoice_EmptyDetails() {
		Supplier s = getExistingSupplier();
		Assert.assertNotNull(s);

		ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #8");
		// No details added
		Assert.assertEquals(0, inv.getDetails().size());
		Assert.assertEquals(0f, inv.getTotalAmount(), 0.01f);
	}

	/**
	 * Test case #9: Cancel import (no DB operation)
	 * Simply verify that ImportInvoice object is not persisted.
	 */
	@Test
	public void testCancelImport() {
		Supplier s = getExistingSupplier();
		Material m = getExistingMaterial("Kem");

		ImportInvoice inv = new ImportInvoice(s, getDummyUser(), new Date(), "Test invoice #9 - cancelled");
		inv.getDetails().add(new ImportInvoiceDetail(m, 5, m.getUnitPrice()));

		// Simulate cancel: do NOT call addImportInvoice
		Assert.assertEquals(0, inv.getId()); // id is still 0 (not saved)
	}
}
