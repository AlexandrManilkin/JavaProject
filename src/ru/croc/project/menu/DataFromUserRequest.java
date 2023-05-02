package ru.croc.project.menu;


import java.util.Scanner;

/**
 * Класс для получения данных от пользователя
 */
public class DataFromUserRequest {

    private final String requestMessage;

    private String answerPattern;
    
    private String answerDoesNotMatchPatternMessage;

    private String cancelCommand;

    private static String cancelMessage = "или введите %s для выхода";

    public DataFromUserRequest(String requestMessage){
        this.requestMessage = requestMessage;
    }

    public void setAnswerPattern(String pattern, String message){
        if(pattern == null || message == null) return;
        answerDoesNotMatchPatternMessage = message;
        answerPattern = pattern;
    }

    public void setCancelCommand(String command){
        cancelCommand = command;
    }

    public String requestData(){
        System.out.println(); // отделяем от предыдущих выводов для лучшей читаемости
        String request = requestMessage +
                (cancelCommand != null ? " " + String.format(cancelMessage, cancelCommand) : "");
        System.out.println(request);
        var scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        if (answerPattern == null) return answer;

        while (!answer.matches(answerPattern) && (!answer.equals(cancelCommand))){
            System.out.println(answerDoesNotMatchPatternMessage);
            answer = scanner.nextLine();
        }

        if(answer.equals(cancelCommand)){
            throw new OperationCanceledException();
        }

        return answer;
    }





}
