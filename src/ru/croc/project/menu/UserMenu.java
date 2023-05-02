package ru.croc.project.menu;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import ru.croc.project.database.DataBaseSocket;
import ru.croc.project.menu.selectItem.SelectItemRequest;
import ru.croc.project.menu.selectItem.templates.ActionsWithTest;
import ru.croc.project.menu.selectItem.templates.AdministratorMainMenuItems;
import ru.croc.project.menu.selectItem.templates.TestCompletedActions;
import ru.croc.project.menu.selectItem.templates.UserMainMenuItems;
import ru.croc.project.tests.CompletedTestContainer;
import ru.croc.project.tests.Exercise;
import ru.croc.project.tests.Test;
import ru.croc.project.tests.TestProcess;
import ru.croc.project.users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Класс реализует логику навигации по меню
 */
public class UserMenu {
    public static void logIn(User user){
        System.out.printf("Привет, %s \n", user.getName());
        if(user.getAdministratorStatus()){
            printAdministratorMainMenu(user);
        }else{
            printUserMenu(user);
        }
    }

    private static void printAdministratorMainMenu(User user){
        while(true) {
            try {
                var menu = new SelectItemRequest<>(
                        "Выберите необходимое действие",
                        AdministratorMainMenuItems.values(),
                        true
                );
                var answer = menu.makeRequest();
                switch (answer){
                    case GET_TEST_LIST:
                        printTestList();
                        break;
                    case ADD_TEST:
                        addTestMenu();
                        break;
                    case GET_USERS_STATISTICS:
                        selectUserForGettingStatistics();
                        break;
                    case START_TEST:
                        selectTestToPass(user);
                        break;
                }
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void printTestList(){
        while (true){
            try {
                var testSelectRequest = new SelectItemRequest<>(
                        "Доступные тесты",
                        DataBaseSocket.getSocket().getTestList(),
                        true);
                var selectedTest = testSelectRequest.makeRequest();
                printActionsWithTestMenu(selectedTest);
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void printActionsWithTestMenu(Test test){
        while (true){
            try{
                var actionsWithTestRequest = new SelectItemRequest<>(
                        "Выберите нужное действие",
                        ActionsWithTest.values(),
                        true
                );
                var actionWithTest = actionsWithTestRequest.makeRequest();
                switch (actionWithTest){
                    case GET_EXERCISE_LIST:
                        printTestExercises(test);
                        break;
                    case CSV_EXPORT:
                        exportToCSV(test);
                        return;
                    case REMOVE_TEST:
                        DataBaseSocket.getSocket().removeTest(test);
                        return;
                }
            }catch (OperationCanceledException e){
                return;
            }
        }

    }

    private static void printTestExercises(Test test){
        while (true){
            try {
                var exerciseSelectRequest = new SelectItemRequest<>(
                        String.format("Упражнения в тесте %s", test.toString()),
                        test.getExercises(),
                        true
                );
                var selectedExercise = exerciseSelectRequest.makeRequest();
                printExerciseWithTrueAnswer( selectedExercise);
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void exportToCSV(Test test){
        String fileName = String.format("%s.csv", test.getName());
        File file = new File(fileName);
        try(FileWriter writer = new FileWriter(file, false))
        {
            writer.write(test.toStringCSV());
            writer.flush();
            System.out.printf("Тест %s экспортирован в файл %s \n", test.getName(), fileName);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void printExerciseWithTrueAnswer(Exercise exercise){
        while (true){
            try {
                var actionsWithExerciseRequest = new SelectItemRequest<Exercise>(
                        exercise.toStringWithAnswerOptions(),
                        List.of(),
                        true
                );
                actionsWithExerciseRequest.makeRequest();
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void addTestMenu(){
        while (true){
            var testFileNameRequest = new DataFromUserRequest("Введите путь к файлу");
            testFileNameRequest.setAnswerPattern(".*\\.json", "Нужно ввести файл с расширением .json");
            testFileNameRequest.setCancelCommand("0");
            try{
                var fileName = testFileNameRequest.requestData();
                var test = Test.createFromJsonFile(new File(fileName));
                DataBaseSocket.getSocket().addTest(test);
                System.out.println("Тест успешно добавлен");
                return;
            }catch (OperationCanceledException e){
                return;
            }catch (JsonEOFException | UnrecognizedPropertyException e) {
                System.out.println(e.getMessage());
            }catch (IOException e){
                System.out.println("Ошибка");
            }
        }
    }

    private static void selectUserForGettingStatistics(){
        while (true){
            try {
                var userSelectRequest = new SelectItemRequest<>(
                        "Выберите пользователя",
                        DataBaseSocket.getSocket().getUserList(),
                        true
                );
                var selectedUser = userSelectRequest.makeRequest();
                printUsersStatisticsMenu(selectedUser);
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void printUsersStatisticsMenu(User user){
        while (true){
            try {
                var testSelectRequest = new SelectItemRequest<>(
                        "Выберите тест для более подробной статистики",
                        user.getCompletedTests(),
                        true
                );
                var selectedTest = testSelectRequest.makeRequest();
                showTestStatistics(selectedTest);
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void selectTestToPass(User user){
        while(true){
            try{
                var testSelectRequest = new SelectItemRequest<>(
                        "Выберите тест",
                        user.getAvailableTests(),
                        true
                );
                var selectedTest = testSelectRequest.makeRequest();
                showTestResults(TestProcess.startTesting(user, selectedTest));
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

    private static void showTestResults(CompletedTestContainer testContainer){
        var selectActionRequest = new SelectItemRequest<>(
                testContainer.resultsToString(),
                TestCompletedActions.values(),
                true
        );
        var selectedAction = selectActionRequest.makeRequest();
        switch (selectedAction){
            case BEGIN_NEW_TEST:
                return;
            case SHOW_STATISTICS:
                showTestStatistics(testContainer);
        }
    }

    private static void showTestStatistics(CompletedTestContainer testContainer) {
        var showStatisticsRequest = new SelectItemRequest<>(
                testContainer.toStringWithUserAnswers(),
                List.of(),
                true
        );
        showStatisticsRequest.makeRequest();
    }

    private static void printUserMenu(User user){
        while(true) {
            try{
                var menu = new SelectItemRequest<>(
                        "Выберите необходимое действие",
                        UserMainMenuItems.values(),
                        true
                );
                var answer = menu.makeRequest();
                switch (answer){
                    case START_TEST:
                        selectTestToPass(user);
                        break;
                    case GET_STATISTICS:
                        printUsersStatisticsMenu(user);
                }
            }catch (OperationCanceledException e){
                return;
            }
        }
    }

}
