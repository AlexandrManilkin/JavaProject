package ru.croc.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для создания таблиц базы данных
 */
public class Init {
    /**
     *
     * @param args Параметры доступа к БД и SQL скрипт
     * args[0] - url
     * args[1] - user
     * args[2] - pass
     * args[3] - путь к SQL скрипту
     */
    public static void main(String[] args) {
        if(args.length < 4){
            System.out.println("Недостаточно аргументов командной строки");
            return;
        }
        String url = args[0];
        String login = args[1];
        String pass = args[2];
        String scriptFilePass = args[3];
        try (Connection connection = DriverManager.getConnection(url, login, pass)) {
            String script = Files.readString(Paths.get(scriptFilePass));
            try (Statement statement = connection.createStatement()) {
                 statement.execute(script);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        } catch (IOException e){
            System.out.println("Ошибка при работе с фалом скрипта");
        }
    }

}
