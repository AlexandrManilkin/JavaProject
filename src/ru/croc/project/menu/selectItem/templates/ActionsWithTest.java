package ru.croc.project.menu.selectItem.templates;

public enum ActionsWithTest {
    GET_EXERCISE_LIST("Список заданий"),
    CSV_EXPORT("Экспорт в CSV-файл"),
    REMOVE_TEST("Удалить тест");

    private final String label;

    ActionsWithTest(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
