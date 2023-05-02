package ru.croc.project.tests;

import ru.croc.project.database.DataBaseSocket;
import ru.croc.project.menu.selectItem.SelectItemRequest;
import ru.croc.project.users.User;

/**
 * Класс реализует логику тестирования
 */
public class TestProcess {

    public static CompletedTestContainer startTesting(User user, Test test){
        int [] userAnswers = new int[test.getExercises().length];
        Exercise[] exercises = test.getExercises();
        for (int i = 0; i < exercises.length; i++) {
            var selectAnswerRequest = new SelectItemRequest<>(
                    exercises[i].getTask(),
                    exercises[i].getAnswerOptions(),
                    false
            );
            userAnswers[i] = selectAnswerRequest.makeRequestReturnIndex() - 1;
        }
        var completedTest = new CompletedTestContainer(test, userAnswers);
        DataBaseSocket.getSocket().addUserCompletedTest(user, completedTest);
        return completedTest;
    }
}
