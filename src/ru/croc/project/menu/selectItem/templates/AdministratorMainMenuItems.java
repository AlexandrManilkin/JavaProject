package ru.croc.project.menu.selectItem.templates;

public enum AdministratorMainMenuItems {

    ADD_TEST("Добавить тест"),
    GET_TEST_LIST("Список тестов"),
    GET_USERS_STATISTICS("Статистика пользователей"),
    START_TEST("Начать тестирование");


    private final String label;

    AdministratorMainMenuItems(String label) {
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
