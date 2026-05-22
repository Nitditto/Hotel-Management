-- =============================================
-- Module: Importing Materials
-- Database: importing_materials (MySQL, port 3306)
-- =============================================

-- Sử dụng database importing_materials
USE importing_materials;

-- -- Bảng nhân viên (Staff)
-- CREATE TABLE IF NOT EXISTS tblStaff (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(100) NOT NULL,
--     password VARCHAR(100) NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     role VARCHAR(100)
-- );

-- -- Bảng nhà cung cấp
-- CREATE TABLE IF NOT EXISTS tblSupplier (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     address VARCHAR(500),
--     tel VARCHAR(20),
--     email VARCHAR(255)
-- );

-- -- Bảng vật tư (theo báo cáo: id, name, category, unitPrice)
-- CREATE TABLE IF NOT EXISTS tblMaterial (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     category VARCHAR(100),
--     unitPrice FLOAT
-- );

-- -- Phiếu nhập hàng (header)
-- CREATE TABLE IF NOT EXISTS tblImportInvoice (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     idsupplier INT NOT NULL,
--     idcreator INT NOT NULL,
--     importDate DATETIME NOT NULL,
--     note VARCHAR(500),
--     FOREIGN KEY (idsupplier) REFERENCES tblSupplier(id),
--     FOREIGN KEY (idcreator) REFERENCES tblStaff(id)
-- );

-- -- Chi tiết phiếu nhập (detail lines)
-- CREATE TABLE IF NOT EXISTS tblImportInvoiceDetail (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     idinvoice INT NOT NULL,
--     idmaterial INT NOT NULL,
--     quantity FLOAT NOT NULL,
--     unitPrice FLOAT NOT NULL,
--     FOREIGN KEY (idinvoice) REFERENCES tblImportInvoice(id),
--     FOREIGN KEY (idmaterial) REFERENCES tblMaterial(id)
-- );

-- -- =============================================
-- -- Sample data for testing
-- -- =============================================

-- -- Thêm tài khoản nhân viên để login
-- INSERT INTO tblStaff (username, password, name, role) VALUES
-- ('truongnv', '123456', 'Nguyen Van Truong', 'inventoryStaff');

-- -- Thêm nhà cung cấp mẫu
-- INSERT INTO tblSupplier (name, address, tel, email) VALUES
-- ('Cong ty My pham ABC', '123 Nguyen Trai, Q.1, HCM', '0901234567', 'abc@supplier.com'),
-- ('Nha phan phoi XYZ', '456 Le Loi, Q.3, HCM', '0912345678', 'xyz@supplier.com'),
-- ('Dai ly Vat tu Spa Pro', '789 Tran Hung Dao, Q.5, HCM', '0923456789', 'spapro@supplier.com');

-- -- Thêm vật tư mẫu
-- INSERT INTO tblMaterial (name, category, unitPrice) VALUES
-- ('Kem duong da mat', 'Skincare', 250000),
-- ('Dau goi thao duoc', 'Haircare', 180000),
-- ('Thuoc nhuom toc', 'Haircare', 350000),
-- ('Tinh dau massage', 'Body care', 200000),
-- ('Sap wax long', 'Body care', 150000);
