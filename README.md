# Описание проекта

## Общие сведения
Приложение для тестирования по английскому языку. </br>
Авторизация происходит по логину пользователя. </br>
Если введен незарегистрированный логин, система предлагает создать нового пользователя с заданным именем.</br>
Тестовая система представляет собой набор тестов, каждый из которых включает в себя несколько упражнений, предлагающих вставить пропущенное слово или фразу в предложение.</br>
Добавлять и удалять тесты может администратор.</br> 

## Используемые библиотеки

- h2 2.1.214
- jackson 2.13.4

## Запуск программы

1. Перед запуском программы необходимо создать и запустить базу данных H2
2. Запустить программу через класс **ru.croc.project.Init**. </br>
    В качестве аргументов необходимо передать: </br>    
   - URL базы данных
   - имя пользователя БД
   - пароль пользователя БД
   - путь к SQL-скрипту (**/WorkDirectories/init.sql**) </br>

    Таким образом будут созданы необходимые таблицы в БД. 
    Также будет зарегистрирован пользователь с логином **"admin"** и правами администратора.</br>
    Данное действие необходимо делать только 1 раз после создания БД
3. Запустить программу через класс **ru.croc.project.Main**/ (аргументы командной строки такие же,
как и в для **ru.croc.project.Init**, только без SQL-скрипта).

### Структура полученной БД:

![@db_struct](./images/db/db_structure.png)

## Пример запуска программы
### Начало:
![@begin](./images/menu/img.png)
### Главное меню:
![@begin](./images/menu/admin/img.png)
### Добавить тест:
Тут необходимо ввести путь до JSON-файла. </br>
![@begin](./images/menu/admin/adding_test/img.png) </br>
Пример JSON-файла: **/WorkDirectories/test1.json** </br>
В JSON-файле указываются:
- Название теста
- Массив заданий

Каждое задание включает в себя:
- Предложение с пропущенным словом, замененным на "_"
- Массив вариантов ответов
- Номер правильного варианта ответа (начиная с 0)

### Список тестов:
Тут выводится список загруженных тестов: </br>
![@begin](./images/menu/admin/test_list/img.png) </br>
При выборе тесте выводится меню действий над тестом: </br>
![@begin](./images/menu/admin/test_list/test_selected/img.png) </br>
__Список заданий__: </br>
![@begin](./images/menu/admin/test_list/test_selected/exercise_list/img.png) </br>
При выборе задания: </br>
![@begin](./images/menu/admin/test_list/test_selected/exercise_list/exercise_selected/img.png) </br>
При выборе экспорта в CSV-файл, тест экспортируется в рабочую директорию, название файла соответствует названию теста. </br>
### Статистика пользователей:
При выборе данного пункта выводится список пользователей, доступных в системе. </br>
![@begin](./images/menu/admin/users_statistics/img.png) </br>
При выборе пользователя выводится список пройденных тестов с результатами. </br>
![@begin](./images/menu/admin/users_statistics/user_selected/img.png) </br>
При выборе теста выводится отчет по каждому заданию теста </br>
![@begin](./images/menu/admin/users_statistics/user_selected/test_selected/img.png) </br>
### Начать тестирование:
При выборе данного пункта пользователю предлагается выбрать тест из доступных </br>
![@begin](./images/menu/admin/start_test/img.png) </br>
После выбора теста поочереди начинают появляться задания из выбранного теста </br>
![@begin](./images/menu/admin/start_test/test_selected/first_exercise.png) </br>
После окончания теста выводятся результаты </br>
![@begin](./images/menu/admin/start_test/test_selected/test_results.png) </br>
По желанию пользователь может посмотреть статистику по пройденному тесту </br>
![@begin](./images/menu/admin/start_test/test_selected/statistics.png) </br>
### Добавление нового пользователя
Если при входе введено незарегистрированное имя пользователя, система предложит создать нового пользователя. </br>
![@begin](./images/menu/user/adding.png) </br>
Пользователи добавленные таким образом не обладают параметрами администратора, и их главное меню выглядит так </br>
![@begin](./images/menu/user/main_menu.png) </br>
Статистика для обычного пользователя отображается также, как и для администратора





