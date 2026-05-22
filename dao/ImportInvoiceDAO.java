package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import model.ImportInvoice;
import model.ImportDetail;

public class ImportInvoiceDAO extends DAO {

	public ImportInvoiceDAO() {
		super();
	}

	/**
	 * Add an import invoice with all its detail lines in a single transaction.
	 * If any step fails, the entire transaction is rolled back.
	 * 
	 * @param inv the import invoice to add (must have supplier, creator, date, and
	 *            at least 1 detail)
	 * @return true if the invoice was saved successfully
	 */
	public boolean addImportInvoice(ImportInvoice inv) throws Exception {
		String sqlAddInvoice = "INSERT INTO tblImportInvoice(idsupplier, idcreator, importDate, note) VALUES(?,?,?,?)";
		String sqlAddDetail = "INSERT INTO tblImportDetail(idinvoice, idmaterial, quantity, unitPrice) VALUES(?,?,?,?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean result = true;
		boolean initialAutoCommit = true;
		Savepoint savepoint = null;
		try {
			initialAutoCommit = con.getAutoCommit();
			if (initialAutoCommit) {
				con.setAutoCommit(false);
			} else {
				savepoint = con.setSavepoint();
			}

			// Insert invoice header
			PreparedStatement ps = con.prepareStatement(sqlAddInvoice, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, inv.getSupplier().getId());
			ps.setInt(2, inv.getCreator().getId());
			ps.setString(3, sdf.format(inv.getImportDate()));
			ps.setString(4, inv.getNote());

			ps.executeUpdate();
			// Get generated invoice id
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				inv.setId(generatedKeys.getInt(1));

				// Insert detail lines
				for (ImportDetail detail : inv.getDetails()) {
					ps = con.prepareStatement(sqlAddDetail, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, inv.getId());
					ps.setInt(2, detail.getMaterial().getId());
					ps.setInt(3, detail.getQuantity());
					ps.setFloat(4, detail.getUnitPrice());

					ps.executeUpdate();
					ResultSet detailKeys = ps.getGeneratedKeys();
					if (detailKeys.next()) {
						detail.setId(detailKeys.getInt(1));
					}
				}
			} else {
				throw new IllegalStateException("Failed to create import invoice header");
			}

			if (result && initialAutoCommit) {
				con.commit();
			}
		} catch (Exception e) {
			result = false;
			try {
				if (initialAutoCommit) {
					con.rollback();
				} else if (savepoint != null) {
					con.rollback(savepoint);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			throw e;
		} finally {
			try {
				if (initialAutoCommit) {
					con.setAutoCommit(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
