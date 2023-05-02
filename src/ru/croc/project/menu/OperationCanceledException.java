package ru.croc.project.menu;

import ru.croc.project.menu.selectItem.SelectItemRequest;

/**
 * Исключение, бросаемое экземплярами классов {@link SelectItemRequest} и {@link DataFromUserRequest}, когда пользователь отменил операцию
 */
public class OperationCanceledException extends RuntimeException{
}
