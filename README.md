# 📚 BookVault – Organize. Borrow. Read. Repeat.

## 🚀 Overview
The **BookVault** is a robust Java-based application built using JDBC for database connectivity and MySQL for persistent storage. This system is designed to streamline the everyday operations of a library, allowing both administrators and users to manage books, track borrowing activities, and maintain accurate records efficiently.

---

## 🏛️ Database Schema

### 🔹 Users Table
```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE
);
```

### 🔹 Authors Table
```sql
CREATE TABLE authors (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);
```

### 🔹 Languages Table
```sql
CREATE TABLE languages (
    language_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
```

### 🔹 Books Table
```sql
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id INT,
    language_id INT,
    genre VARCHAR(255) NOT NULL,
    rating FLOAT DEFAULT 0.0,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (author_id) REFERENCES authors(author_id),
    FOREIGN KEY (language_id) REFERENCES languages(language_id)
);
```

### 🔹 Borrowed Books Table
```sql
CREATE TABLE borrowed_books (
    borrow_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);
```

---

## 🛠️ Technologies Used
- **Java** 🖥️
- **JDBC (Java Database Connectivity)** 🔗
- **MySQL Database** 🗄️
- **Console-based UI** 📜

---

## 📌 Features
✅ **Admin Login & Dashboard** 🔑  
✅ **User Registration & Authentication** 👥  
✅ **Book Management (Add, View, Delete)** 📖  
✅ **Author & Language Management** 🖊️  
✅ **Borrow & Return Books** 🔄  
✅ **Due Date Tracking** ⏳  
✅ **Search Books by Title, Author, Genre** 🔍  
✅ **Data Persistence with MySQL** 🗄️  

---

## 🔧 Setup Instructions
### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-repo/library-management.git
```

### 2️⃣ Import SQL Database
- Open **MySQL Workbench** or any MySQL client.
- Run the provided SQL script to create tables.

### 3️⃣ Configure Database Connection
- Modify `ConnectionClass.java` with your **MySQL credentials**:
```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 4️⃣ Compile & Run the Application
```bash
javac Main.java
java Main
```

---

## 🏆 Future Enhancements
🚀 **GUI Implementation using JavaFX/Swing**  
🚀 **Advanced Search & Filtering**  
🚀 **Book Reservation System**  
🚀 **Email Notifications for Due Books**  

📢 *Have suggestions? Feel free to contribute!*
📧 anugupta5102@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/anushree-gupta-832410239/) | [GitHub](https://github.com/Anugupta5102)

---

## 📜 License
This project is licensed under the **MIT License**. Feel free to use and modify it!

