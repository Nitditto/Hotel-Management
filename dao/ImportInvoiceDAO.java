package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import model.ImportInvoice;
import model.ImportInvoiceDetail;

public class ImportInvoiceDAO extends DAO {

	public ImportInvoiceDAO() {
		super();
	}

	/**
	 * Add an import invoice with all its detail lines in a single transaction.
	 * If any step fails, the entire transaction is rolled back.
	 * @param inv the import invoice to add (must have supplier, creator, date, and at least 1 detail)
	 * @return true if the invoice was saved successfully
	 */
	public boolean addImportInvoice(ImportInvoice inv) {
		String sqlAddInvoice = "INSERT INTO tblImportInvoice(idsupplier, idcreator, importDate, note) VALUES(?,?,?,?)";
		String sqlAddDetail = "INSERT INTO tblImportInvoiceDetail(idinvoice, idmaterial, quantity, unitPrice) VALUES(?,?,?,?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean result = true;
		try {
			con.setAutoCommit(false);

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
				for (ImportInvoiceDetail detail : inv.getDetails()) {
					ps = con.prepareStatement(sqlAddDetail, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, inv.getId());
					ps.setInt(2, detail.getMaterial().getId());
					ps.setFloat(3, detail.getQuantity());
					ps.setFloat(4, detail.getUnitPrice());

					ps.executeUpdate();
					ResultSet detailKeys = ps.getGeneratedKeys();
					if (detailKeys.next()) {
						detail.setId(detailKeys.getInt(1));
					}
				}
			}

			con.commit();
		} catch (Exception e) {
			result = false;
			try {
				con.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
