package ru.croc.project.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Класс для хранения данных упражнения
 */
public class Exercise {

    public String task;
    public String[] answerOptions;
    public int trueAnswerId;
    /**
     * Идентификатор
     */
    private int id;

    public Exercise(){

    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public void setTask(String task){
        this.task = task;
    }
    public String getTask(){
        return task;
    }
    public void setAnswerOptions(String[] options){
        answerOptions = options;
    }
    public String[] getAnswerOptions(){
        return answerOptions;
    }
    public void setTrueAnswerId(int id){
        trueAnswerId = id;
    }
    public int getTrueAnswerId(){
        return trueAnswerId;
    }

    @Override
    public String toString(){
        return task.replaceAll("_+", "_" + answerOptions[trueAnswerId] + "_");
    }

    public String toStringWithAnswerOptions(){
        var builder = new StringBuilder();
        builder.append(task);
        for (int i = 0; i < answerOptions.length; i++) {
            builder.append(String.format(
                    "\n\t - %s %s",
                    answerOptions[i],
                    i == trueAnswerId ? "(true)" : ""));
        }

        return builder.toString();
    }

    public String toStringCSV(){
        var builder = new StringBuilder();
        builder.append(task);
        for (int i = 0; i < answerOptions.length; i++) {
            builder.append(String.format(
                    ",%s%s",
                    answerOptions[i],
                    i == trueAnswerId ? "(true)" : ""));
        }
        return builder.toString();
    }
}
