package ru.croc.project;

import ru.croc.project.database.DataBaseSocket;
import ru.croc.project.menu.DataFromUserRequest;
import ru.croc.project.menu.OperationCanceledException;
import ru.croc.project.menu.UserMenu;
import ru.croc.project.menu.selectItem.SelectItemRequest;
import ru.croc.project.users.User;

import java.util.List;

public class Main {
    /**
     *
     * @param args Параметры доступа к БД и SQL скрипт
     * args[0] - url
     * args[1] - user
     * args[2] - pass
     */
    public static void main(String[] args) {
        if(args.length < 3){
            System.out.println("Недостаточно аргументов командной строки");
            return;
        }
        String url = args[0];
        String login = args[1];
        String pass = args[2];
        DataBaseSocket.initSocket(url, login, pass);
        var dataBase = DataBaseSocket.getSocket();
        while (true) {
            String message = "Введите имя пользователя";
            var newRequest = new DataFromUserRequest(message);
            newRequest.setAnswerPattern("\\w+", "Имя пользователя должно состоять из одного слова");
            newRequest.setCancelCommand("0");
            String username;
            try {
                username = newRequest.requestData();
            } catch (OperationCanceledException e) {
                return;
            }
            User currentUser = dataBase.getUserList().stream()
                    .filter(user -> user.getName().equals(username))
                    .findAny()
                    .orElse(null);
            if (currentUser == null) {
                var newAccountCreatingRequest = new SelectItemRequest<>(
                        "Пользователя с таким именем не существует, создать новый аккаунт?",
                        List.of("Да", "Ввести имя пользователя заново"),
                        false
                );
                switch (newAccountCreatingRequest.makeRequest()) {
                    case ("Да"):
                        dataBase.addUser(username);
                        currentUser = dataBase.getUserList().stream()
                                .filter(user -> user.getName().equals(username))
                                .findAny()
                                .orElse(null);
                        break;
                    case ("Ввести имя пользователя заново"):
                        continue;
                }
            }
            UserMenu.logIn(currentUser);
        }


    }
}
