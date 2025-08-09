# 🎓 Student Management System (Java + MySQL + Swing)

A basic Student Management System built using **Java**, **MySQL**, and **Swing GUI**, where users (Admins, Teachers, and Students) can log in, view dashboards, manage data, and interact — all through a desktop-based interface.

> ✅ This project was developed and executed using **VS Code** with Java support and MySQL.

---

## 🚀 Features

### 👩‍💼 Admin Dashboard
- Add, view, update, delete, and search student records
- Add and view teacher records
- Logout

### 👨‍🏫 Teacher Dashboard
- Mark attendance
- View all students
- Assign and view grades
- Messaging system (to/from students)
- Logout

### 👩‍🎓 Student Dashboard
- View grades and attendance
- Messaging system (to/from teachers)
- Logout

---

## 🛠️ Requirements & Setup Instructions

Before running the project, ensure you have the following installed:

### ✅ Prerequisites

| Tool               | Version (Recommended) | Description |
|--------------------|------------------------|-------------|
| [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) | 8 or above          | To compile and run Java |
| [MySQL Server](https://dev.mysql.com/downloads/installer/) | 8.0                | For storing and managing the database |
| [VS Code](https://code.visualstudio.com/) or any Java IDE | Latest             | To write and execute the code |
| [MySQL JDBC Driver (Connector/J)](https://dev.mysql.com/downloads/connector/j/) | Latest              | For Java-MySQL database connection |

---

### 🔧 Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/donthireddysaivarshini/Student-Management-System.git
   ```

2. **Set Up the MySQL Database**
   - Open MySQL Workbench or terminal.
   - Import the file `StudentManagement.sql` located in the project folder.
   - This will create the required tables and insert sample data (admin, teachers, students).

3. **Update Database Credentials**
   - Open the `DBConnection.java` file in VS Code.
   - Edit the following lines with your own MySQL username and password:
     ```java
     String user = "your_mysql_username";
     String password = "your_mysql_password";
     ```

4. **Download and Add JDBC Driver**
   - Download the `.jar` file: [JDBC Connector](https://dev.mysql.com/downloads/connector/j/)
   - In VS Code:
     - Go to Command Palette → Java: Configure Java Runtime
     - Add the downloaded `.jar` to your project’s referenced libraries or classpath

5. **Run the Application**
   - Open `LoginPage.java` in VS Code.
   - Press `Run` (ensure JDBC connector is set up)
   - Or use terminal:
     ```bash
     javac LoginPage.java
     java LoginPage
     ```

---

## 📹 Demo Video

🎥 Watch the full project demo here on LinkedIn:  
![Demo Video](Demo.mp4)

---

## 🔮 Future Improvements

- Upgrade UI from Swing to JavaFX or a Web-based frontend
- Add charts for performance and attendance tracking
- Improve the messaging system UI and add real-time alerts
- Deploy database on cloud (AWS RDS, Firebase, etc.)
- Add login authentication enhancements (e.g., hashed passwords)

---

## 🙋‍♀️ Author

**Donthireddy Saivarshini**  
Dept. of CSE-IoT, Malla Reddy Engineering College for Women  
📧 dsaivarshini30@gmail.com  
🌐 [LinkedIn](https://www.linkedin.com/in/donthireddysaivarshini)

---

## 📄 License

This project is available for educational and learning purposes.  
Feel free to use, modify, or share it — a small credit would be appreciated! 😊
