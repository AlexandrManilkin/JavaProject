package ru.croc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Application {
    public static void main(String[] args) {
        String url = "jdbc:h2:tcp://localhost:9092/~/IdeaProjects/Database/db/test";

        try (Connection conn = DriverManager.getConnection(url, "admin", "")) {
            String sql = "select * from students";
            try (Statement statement = conn.createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String surname = resultSet.getString("SURNAME");
                            String firstname = resultSet.getString("FIRSTNAME");

                            String format = String.format("%d, %s %s", id, surname, firstname);
                            System.out.println(format);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }
}
