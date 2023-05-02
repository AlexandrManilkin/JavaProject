package ru.croc.project.menu.selectItem.templates;

public enum TestCompletedActions {

    SHOW_STATISTICS("Показать статистику"),
    BEGIN_NEW_TEST("Начать новый тест");


    private final String label;

    TestCompletedActions(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
