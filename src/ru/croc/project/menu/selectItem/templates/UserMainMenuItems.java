package ru.croc.project.menu.selectItem.templates;

public enum UserMainMenuItems {
    START_TEST("Начать тестирование"),
    GET_STATISTICS("Статистика");


    private final String label;

    UserMainMenuItems(String label) {
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
