-- Phân quyền
CREATE TABLE Roles (
    ID SERIAL PRIMARY KEY,
    RoleName VARCHAR(50) NOT NULL,
    LevelRole int NOT NULL
);

-- Tài khoản người dùng
CREATE TABLE Users (
    ID SERIAL PRIMARY KEY,
    Username VARCHAR(255) UNIQUE NOT NULL,
    PasswordHash TEXT NOT NULL,
    FullName VARCHAR(255) NOT NULL,
    PhoneNumber VARCHAR(15) UNIQUE NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    Address TEXT,
    Image TEXT NOT NULL,
    DateOfBirth DATE NOT NULL DEFAULT CURRENT_DATE,
    Gender VARCHAR(10) CHECK (Gender IN ('Nam', 'Nữ', 'Khác')),
    CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RoleID INT NOT NULL,
    FOREIGN KEY (RoleID) REFERENCES Roles(ID) ON DELETE CASCADE
);

-- Khoa/Phòng ban
CREATE TABLE Departments (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Description TEXT
);

-- Phòng khám
CREATE TABLE Rooms (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    DepartmentID INT,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(ID) ON DELETE SET NULL
);

-- Bệnh nhân
CREATE TABLE Patients (
    ID SERIAL PRIMARY KEY,
    UserID INT NOT NULL,
    FullName VARCHAR(255) NOT NULL,
    Age DATE NOT NULL,
    Gender VARCHAR(10) CHECK (Gender IN ('Nam', 'Nữ', 'Khác')),
    Address TEXT NOT NULL,
    PhoneNumber VARCHAR(15) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE
);

-- Bác sĩ
CREATE TABLE Doctors (
    ID SERIAL PRIMARY KEY,
    Specialization VARCHAR(255) NOT NULL,
    UserID INT NOT NULL,
    DepartmentID INT NOT NULL,
    Experience INT NOT NULL,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(ID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE
);

-- Lịch hẹn
CREATE TABLE Appointments (
    ID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentDate TIMESTAMP NOT NULL,
    Status VARCHAR(20) CHECK (Status IN ('Đợi duyệt', 'Đã duyệt', 'Đã khám', 'Đang điều trị', 'Đợi kết quả', 'Đã khỏi', 'Cần tái khám')),
    Notes TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Checkagain TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(ID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(ID) ON DELETE CASCADE
);

-- Hồ sơ bệnh án
CREATE TABLE MedicalRecords (
    ID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentID INT NOT NULL,
    Diagnosis TEXT NOT NULL,
    Symptom TEXT NOT NULL,
    TreatmentPlan TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(ID) ON DELETE CASCADE,
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(ID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(ID) ON DELETE CASCADE
);

-- Dịch vụ y tế
CREATE TABLE Services (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Description TEXT,
    Image TEXT NOT NULL,
    Price DECIMAL(10,2) NOT NULL
);

-- Dịch vụ sử dụng
CREATE TABLE UseServices (
    ID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    ServicesID INT NOT NULL,
    Price DECIMAL NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(ID) ON DELETE CASCADE,
    FOREIGN KEY (ServicesID) REFERENCES Services(ID) ON DELETE CASCADE
);

-- Thuốc
CREATE TABLE Medicines (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Type VARCHAR(50),
    Manufacturer VARCHAR(255),
    ExpiryDate DATE NOT NULL
);

-- Hóa đơn
CREATE TABLE Invoice (
    ID SERIAL PRIMARY KEY,
    PatientID INT NOT NULL,
    TotalAmount DECIMAL(10,2) NOT NULL,
    Status VARCHAR(20) CHECK (Status IN ('Chưa thanh toán', 'Đã thanh toán')),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES Patients(ID) ON DELETE CASCADE
);

-- Thanh toán
CREATE TABLE Payments (
    ID SERIAL PRIMARY KEY,
    InvoiceID INT NOT NULL,
    AmountPaid DECIMAL(10,2) NOT NULL,
    PaymentMethod VARCHAR(50),
    PaidAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (InvoiceID) REFERENCES Invoice(ID) ON DELETE CASCADE
);

-- Lịch làm việc bác sĩ
CREATE TABLE Schedule (
    ID SERIAL PRIMARY KEY,
    DoctorID INT NOT NULL,
    WorkDay TEXT NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(ID) ON DELETE CASCADE
);

-- Kho thuốc
CREATE TABLE Inventory (
    ID SERIAL PRIMARY KEY,
    MedicineID INT NOT NULL,
    Quantity INT NOT NULL,
    FOREIGN KEY (MedicineID) REFERENCES Medicines(ID) ON DELETE CASCADE
);

-- Đánh giá
CREATE TABLE Feedbacks (
    ID SERIAL PRIMARY KEY,
    UserID INT NOT NULL,
    DoctorID INT NOT NULL,
    Title TEXT,
    Comment TEXT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    TimeCreate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(ID) ON DELETE CASCADE
);

-- Header
CREATE TABLE HeaderLayout (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	url TEXT NOT NULL,
	police VARCHAR(25) NOT NULL,
	isToken VARCHAR(1) NOT NULL,
	isDefault VARCHAR(1) NOT NULL,
  leveSort int NOT NULL
);

-- Loại thông báo
CREATE TABLE TypeNotification (
  id SERIAL PRIMARY KEY,
  NameType VARCHAR(25) NOT NULL
);

-- Thông báo
CREATE TABLE Notification (
	id SERIAL PRIMARY KEY,
	Title TEXT NOT NULL,
	Content TEXT NOT NULL,
	IdType INT NOT NULL,
	isRead bool,
	AppointmentDate DATE,
  AppointmentTime DATE,
  PatientID INT,
  Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (IdType) REFERENCES TypeNotification(ID) ON DELETE CASCADE,
  FOREIGN KEY (PatientID) REFERENCES Patients(ID) ON DELETE CASCADE
);

-- Loại thuốc sử dụng
CREATE TABLE MedicinesInvoice (
  id SERIAL PRIMARY KEY,
  InvoiceID INT,
  IdMedicines INT,
  Quantity INT,
  Price DECIMAL,
  FOREIGN KEY (InvoiceID) REFERENCES Invoice(ID) ON DELETE CASCADE,
  FOREIGN KEY (IdMedicines) REFERENCES Medicines(ID) ON DELETE CASCADE
);