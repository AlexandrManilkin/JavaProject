package ru.croc.project.menu.selectItem;

import ru.croc.project.menu.OperationCanceledException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для выбора из предложенных вариантов
 */
public class SelectItemRequest <T> {

    private static final String canselOptionText = "Выход";
    private static final String nonNumericReceived = "Получено не числовое значение";
    private static final String validInputDataFormat = "Введите значение от %d до %d";
    private final String requestMessage;
    private final List<T> answerOptions;
    private final boolean enableCancelOption;


    public SelectItemRequest(String requestMessage, List<T> options, boolean enableCancelOption) {
        this.requestMessage = requestMessage;
        answerOptions = options;
        this.enableCancelOption = enableCancelOption;
    }

    public SelectItemRequest(String requestMessage, T[] options, boolean enableCancelOption) {
        this(requestMessage, List.of(options), enableCancelOption);
    }

    public int makeRequestReturnIndex(){
        System.out.println(); // отделяем от предыдущих выводов для лучшей читаемости
        System.out.println(requestMessage);
        for (int i = 0; i < answerOptions.size(); i++) {
            System.out.printf("%d) %s \n", i + 1, answerOptions.get(i));
        }

        if (enableCancelOption) {
            System.out.printf("0) %s \n", canselOptionText);
        }

        int answer = -1;
        int minVal = enableCancelOption ? 0 : 1;
        int maxVal = answerOptions.size();
        while (answer < minVal || answer > maxVal) {
            System.out.printf(validInputDataFormat + '\n', minVal, maxVal);
            answer = getIntFromUser();
        }
        if(answer == 0){
            throw new OperationCanceledException();
        }
        return answer;
    }

    public T makeRequest() {
        return answerOptions.get(makeRequestReturnIndex() - 1);
    }

    private int getIntFromUser() {
        while (true) {
            try {
                var scanner = new Scanner(System.in);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(nonNumericReceived);
            }
        }

    }
}
