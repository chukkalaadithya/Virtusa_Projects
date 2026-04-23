-- Digital Library Audit System
-- Book Loan Tracking and Penalty Report

-- creating books table
CREATE TABLE Books (
    BookID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(200) NOT NULL,
    Author VARCHAR(150) NOT NULL,
    Category VARCHAR(50) NOT NULL,
    ISBN VARCHAR(20) UNIQUE,
    TotalCopies INT DEFAULT 1
);

-- creating students table
CREATE TABLE Students (
    StudentID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Email VARCHAR(150) UNIQUE,
    Phone VARCHAR(15),
    EnrollDate DATE NOT NULL
);

-- issued books table to track borrows
CREATE TABLE IssuedBooks (
    IssueID INT PRIMARY KEY AUTO_INCREMENT,
    BookID INT NOT NULL,
    StudentID INT NOT NULL,
    IssueDate DATE NOT NULL,
    ReturnDate DATE DEFAULT NULL,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);

-- inserting books data
INSERT INTO Books (Title, Author, Category, ISBN, TotalCopies) VALUES
('2 States', 'Chetan Bhagat', 'Fiction', '978-8129118192', 3),
('Wings of Fire', 'APJ Abdul Kalam', 'Science', '978-8173711466', 2),
('Discovery of India', 'Jawaharlal Nehru', 'History', '978-0143031031', 4),
('The White Tiger', 'Aravind Adiga', 'Fiction', '978-1416562603', 3),
('Ignited Minds', 'APJ Abdul Kalam', 'Science', '978-0143029571', 2),
('India After Gandhi', 'Ramachandra Guha', 'History', '978-0060958589', 1),
('Immortals of Meluha', 'Amish Tripathi', 'Fiction', '978-9380658742', 2),
('The God of Small Things', 'Arundhati Roy', 'Fiction', '978-0679457312', 3),
('Sapiens', 'Yuval Noah Harari', 'History', '978-0062316097', 2),
('Train to Pakistan', 'Khushwant Singh', 'Fiction', '978-0143065883', 4);

-- inserting student records
INSERT INTO Students (FirstName, LastName, Email, Phone, EnrollDate) VALUES
('Adith', 'Sharma', 'adith.s@college.edu', '9876543210', '2023-08-15'),
('Priya', 'Patel', 'priya.p@college.edu', '9123456789', '2022-01-10'),
('Rahul', 'Verma', 'rahul.v@college.edu', '9988776655', '2024-01-20'),
('Sneha', 'Reddy', 'sneha.r@college.edu', '9871234567', '2021-09-01'),
('Vikram', 'Nair', 'vikram.n@college.edu', '9765432100', '2020-06-15');

-- inserting issued books records
INSERT INTO IssuedBooks (BookID, StudentID, IssueDate, ReturnDate) VALUES
(1, 1, '2026-03-01', NULL),
(3, 2, '2026-04-01', NULL),
(5, 3, '2026-04-15', NULL),
(2, 1, '2026-03-10', '2026-03-20'),
(4, 4, '2026-02-15', '2026-02-25'),
(1, 2, '2026-01-05', '2026-01-12'),
(7, 3, '2026-03-01', '2026-03-10'),
(1, 4, '2025-11-01', '2025-11-14'),
(3, 1, '2025-12-01', '2025-12-10'),
(8, 2, '2026-02-01', '2026-02-14'),
(10, 5, '2025-10-01', '2025-10-15'),
(4, 5, '2025-09-01', '2025-09-14'),
(6, 5, '2022-01-15', '2022-01-28');

-- Query to find overdue books (not returned within 14 days)
SELECT s.StudentID, s.FirstName, s.LastName, s.Email,
    b.Title AS BookTitle, b.Category, ib.IssueDate,
    DATEDIFF(CURDATE(), ib.IssueDate) AS DaysOverdue
FROM IssuedBooks ib
JOIN Students s ON ib.StudentID = s.StudentID
JOIN Books b ON ib.BookID = b.BookID
WHERE ib.ReturnDate IS NULL
AND DATEDIFF(CURDATE(), ib.IssueDate) > 14
ORDER BY DaysOverdue DESC;

-- finding most popular book category
SELECT b.Category, COUNT(ib.IssueID) AS TotalBorrows
FROM IssuedBooks ib
JOIN Books b ON ib.BookID = b.BookID
GROUP BY b.Category
ORDER BY TotalBorrows DESC;

-- checking which students are inactive (no borrow in 3 years)
SELECT s.StudentID, s.FirstName, s.LastName,
    MAX(ib.IssueDate) AS LastBorrowDate
FROM Students s
LEFT JOIN IssuedBooks ib ON s.StudentID = ib.StudentID
GROUP BY s.StudentID, s.FirstName, s.LastName
HAVING MAX(ib.IssueDate) < DATE_SUB(CURDATE(), INTERVAL 3 YEAR)
    OR MAX(ib.IssueDate) IS NULL;

-- deleting issued records of inactive students first
DELETE FROM IssuedBooks
WHERE StudentID IN (
    SELECT StudentID FROM (
        SELECT s.StudentID
        FROM Students s
        LEFT JOIN IssuedBooks ib ON s.StudentID = ib.StudentID
        GROUP BY s.StudentID
        HAVING MAX(ib.IssueDate) < DATE_SUB(CURDATE(), INTERVAL 3 YEAR)
            OR MAX(ib.IssueDate) IS NULL
    ) AS inactive
);

-- now deleting inactive students
DELETE FROM Students
WHERE StudentID IN (
    SELECT StudentID FROM (
        SELECT s.StudentID
        FROM Students s
        LEFT JOIN IssuedBooks ib ON s.StudentID = ib.StudentID
        GROUP BY s.StudentID
        HAVING MAX(ib.IssueDate) IS NULL
    ) AS inactive
)
OR StudentID NOT IN (SELECT DISTINCT StudentID FROM IssuedBooks);
