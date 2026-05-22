CREATE DATABASE IF NOT EXISTS importing_materials;
USE importing_materials;

-- Xóa các bảng cũ nếu tồn tại (để tránh lỗi tạo lại hoặc khóa ngoại)
DROP TABLE IF EXISTS tblImportDetail;
DROP TABLE IF EXISTS tblImportInvoice;
DROP TABLE IF EXISTS tblStaff;
DROP TABLE IF EXISTS tblSupplier;
DROP TABLE IF EXISTS tblMaterial;

-- 1. Bảng nhân viên (Staff) - Đã đổi 'position' thành 'role'
CREATE TABLE tblStaff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(100)
);

-- 2. Bảng nhà cung cấp (Supplier)
CREATE TABLE tblSupplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    tel VARCHAR(20),
    email VARCHAR(255)
);

-- 3. Bảng vật tư (Material)
CREATE TABLE tblMaterial (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    unitPrice FLOAT
);

-- 4. Phiếu nhập hàng (Import Invoice - Header)
CREATE TABLE tblImportInvoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idsupplier INT NOT NULL,
    idcreator INT NOT NULL,
    importDate DATETIME NOT NULL,
    note VARCHAR(500),
    FOREIGN KEY (idsupplier) REFERENCES tblSupplier(id) ON DELETE CASCADE,
    FOREIGN KEY (idcreator) REFERENCES tblStaff(id) ON DELETE CASCADE
);

-- 5. Chi tiết phiếu nhập (Import Detail) - Đã đổi 'quantity' sang INT
CREATE TABLE tblImportDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idinvoice INT NOT NULL,
    idmaterial INT NOT NULL,
    quantity INT NOT NULL,
    unitPrice FLOAT NOT NULL,
    FOREIGN KEY (idinvoice) REFERENCES tblImportInvoice(id) ON DELETE CASCADE,
    FOREIGN KEY (idmaterial) REFERENCES tblMaterial(id) ON DELETE CASCADE
);


-- =============================================
-- DỮ LIỆU MẪU (Sample Data)
-- =============================================

-- Thêm tài khoản nhân viên (sử dụng cột 'role')
INSERT INTO tblStaff (username, password, name, role) VALUES
('truongnv', '123456', 'Nguyen Van Truong', 'inventoryStaff');

-- Thêm nhà cung cấp mẫu
INSERT INTO tblSupplier (name, address, tel, email) VALUES
('Cong ty My pham ABC', '123 Nguyen Trai, Q.1, HCM', '0901234567', 'abc@supplier.com'),
('Nha phan phoi XYZ', '456 Le Loi, Q.3, HCM', '0912345678', 'xyz@supplier.com'),
('Dai ly Vat tu Spa Pro', '789 Tran Hung Dao, Q.5, HCM', '0923456789', 'spapro@supplier.com');

-- Thêm vật tư mẫu
INSERT INTO tblMaterial (name, category, unitPrice) VALUES
('Kem duong da mat', 'Skincare', 250),
('Dau goi thao duoc', 'Haircare', 180),
('Thuoc nhuom toc', 'Haircare', 350),
('Tinh dau massage', 'Body care', 200),
('Sap wax long', 'Body care', 150);
