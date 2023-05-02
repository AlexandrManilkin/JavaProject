package ru.croc.project.users;

import ru.croc.project.database.DataBaseSocket;
import ru.croc.project.tests.CompletedTestContainer;
import ru.croc.project.tests.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения данных пользователя
 */
public class User {
    /**
     * Имя пользователя
     */
    private final String name;
    private final int id;
    private final List<CompletedTestContainer> completedTests;
    private final boolean administratorStatus;

    public User(String name, int id, boolean administratorStatus){
        this.name = name;
        completedTests = new ArrayList<>();
        this.administratorStatus = administratorStatus;
        this.id = id;
    }

    public User(String name, int id, boolean administratorStatus, List<CompletedTestContainer> completedTests){
        this.name = name;
        this.completedTests = completedTests;
        this.administratorStatus = administratorStatus;
        this.id = id;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public boolean getAdministratorStatus(){
        return administratorStatus;
    }

    public List<CompletedTestContainer> getCompletedTests(){
        return completedTests;
    }

    public List<Test> getAvailableTests(){
        return DataBaseSocket.getSocket().getTestList();
    }

    public void addCompletedTest(CompletedTestContainer test){
        completedTests.add(test);
    }

    @Override
    public String toString(){
        return name;
    }

}
