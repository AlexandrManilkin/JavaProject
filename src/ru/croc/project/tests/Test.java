package ru.croc.project.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Класс тесте
 */
public class Test {
    public String name;
    public Exercise[] exercises;
    private int id;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setExercises(Exercise[] exercises){
        this.exercises = exercises;
    }
    public Exercise[] getExercises(){
        return exercises;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
    public String toStringCSV(){
        var builder = new StringBuilder();
        for (Exercise exercise : exercises) {
            builder.append(exercise.toStringCSV() + '\n');
        }
        return builder.toString();
    }
    public static Test createFromJsonFile(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Test test;
        test = mapper.readValue(jsonFile, Test.class);
        return test;
    }
}
