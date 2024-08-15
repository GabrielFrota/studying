package org.example;

import java.sql.*;
import java.time.LocalDate;

public class NewClientsCount {
    public static void main(String[] args) throws SQLException {
        var conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        var stmt = conn.prepareStatement("DROP TABLE IF EXISTS emp_client_stat;" +
                "DROP TABLE IF EXISTS employees;");
        stmt.executeUpdate();

        stmt = conn.prepareStatement("""
            CREATE TABLE employees (
                employee_id INT8 NOT NULL,
                employee_name VARCHAR NOT NULL,
                PRIMARY KEY (employee_id)
            )
        """);
        stmt.execute();

        stmt = conn.prepareStatement("""
            CREATE TABLE emp_client_stat(
                employee_id INT8 NOT NULL ,
                date DATE NOT NULL,
                new_clients_count INT,
                FOREIGN KEY (employee_id) REFERENCES employees (employee_id)   
            )     
        """);
        stmt.execute();

        conn.prepareStatement("INSERT INTO employees VALUES (1, 'John Johnson')").execute();
        conn.prepareStatement("INSERT INTO employees VALUES (2, 'Liam Liamsom')").execute();
        conn.prepareStatement("INSERT INTO employees VALUES (3, 'Smitty Smithson')").execute();
        for (int i = 3; i < 26; i++) {
            stmt = conn.prepareStatement("INSERT INTO emp_client_stat VALUES (1, ?, 1)");
            stmt.setDate(1, Date.valueOf(LocalDate.parse("2021-03-" + String.format("%02d", i))));
            stmt.executeUpdate();

            stmt = conn.prepareStatement("INSERT INTO emp_client_stat VALUES (2, ?, 2)");
            stmt.setDate(1, Date.valueOf(LocalDate.parse("2021-03-" + String.format("%02d", i))));
            stmt.executeUpdate();

            stmt = conn.prepareStatement("INSERT INTO emp_client_stat VALUES (2, ?, 2)");
            stmt.setDate(1, Date.valueOf(LocalDate.parse("2021-02-" + String.format("%02d", i))));
            stmt.executeUpdate();

            stmt = conn.prepareStatement("INSERT INTO emp_client_stat VALUES (3, ?, 3)");
            stmt.setDate(1, Date.valueOf(LocalDate.parse("2021-03-" + String.format("%02d", i))));
            stmt.executeUpdate();

            stmt = conn.prepareStatement("INSERT INTO emp_client_stat VALUES (3, ?, 2)");
            stmt.setDate(1, Date.valueOf(LocalDate.parse("2021-02-" + String.format("%02d", i))));
            stmt.executeUpdate();
        }

        stmt = conn.prepareStatement("""
            SELECT employee_name, SUM(c.new_clients_count)
            FROM employees e JOIN emp_client_stat c ON e.employee_id = c.employee_id
            WHERE MONTH(c.date) = 3
            GROUP BY c.employee_id
        """);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("{employee_name: "+ rs.getString(1) +
                    ", new_clients_count: "+ rs.getInt(2) + "}");
        }
    }
}