package ru.croc.project.database;

import ru.croc.project.tests.CompletedTestContainer;
import ru.croc.project.tests.Exercise;
import ru.croc.project.tests.Test;
import ru.croc.project.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс для взаимодействия с базами данных
 */
public class DataBaseSocket {
    private static String url = "jdbc:h2:tcp://localhost:9092/~/TMP/db/example";
    private static String login = "admin";
    private static String pass = "admin";
    private static final DataBaseSocket socket = new DataBaseSocket();
    private final List<User> users;
    private final List<Test> tests;

    private DataBaseSocket() {
        tests = new ArrayList<>();
        initTests();
        users = new ArrayList<>();
        initUsers();
    }

    public static void initSocket(String url1, String login1, String pass1){
        url = url1;
        login = login1;
        pass = pass1;
    }

    public static DataBaseSocket getSocket() {
        return socket;
    }

    public List<User> getUserList() {
        return users;
    }

    public List<Test> getTestList() {
        return tests;
    }

    public void addUser(String username) {
        try (Connection conn = DriverManager.getConnection(url, login, pass)) {
            String sql = "insert into users (name, status) values (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, "user");
                statement.executeUpdate();
            }

            sql = "select * from users s where s.name = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.last();
                    int id = resultSet.getInt("ID");
                    users.add(new User(username, id, false));
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }

    public void addUserCompletedTest(User user, CompletedTestContainer testContainer) {
        try (Connection connection = DriverManager.getConnection(url, login, pass)) {
            int containerId = insertCompletedTestToDB(connection, user, testContainer);
            for (int userAnswer : testContainer.getUserAnswers()) {
                insertUserAnswerToDB(connection, userAnswer, containerId);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
        user.addCompletedTest(testContainer);
    }

    public void addTest(Test test) {

        try (Connection connection = DriverManager.getConnection(url, login, pass)) {
            int testId = insertTestToDB(connection, test);
            for (Exercise exercise : test.getExercises()) {
                int exerciseId = insertExerciseToDB(connection, exercise, testId);
                String[] answerOptions = exercise.getAnswerOptions();
                for (String answerOption : answerOptions) {
                    insertAnswerOptionToDB(connection, answerOption, exerciseId);
                }
                exercise.setId(exerciseId);
            }
            test.setId(testId);
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
        tests.add(test);
    }


    public void removeTest(Test test) {
        try (Connection connection = DriverManager.getConnection(url, login, pass)) {
            deleteExercisesFromBD(connection, test.getId());
            deleteCompletedTestsFromDB(connection, test.getId());
            String sql = "delete from tests s where s.id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, test.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e){
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
        tests.remove(test);
    }

    private void deleteExercisesFromBD(Connection connection, int testId) throws SQLException{
        String sql = "select * from exercises s where s.test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    int id = resultSet.getInt("ID");
                    deleteAnswerOptionsFromBD(connection, id);
                    deleteCompletedTestsFromDB(connection, id);
                }
            }
        }
        sql = "delete from exercises s where s.test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testId);
            statement.executeUpdate();
        }
    }

    private void deleteAnswerOptionsFromBD(Connection connection, int exerciseId) throws  SQLException{
        String sql = "delete from answer_options s where s.exercise_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, exerciseId);
            statement.executeUpdate();
        }
    }

    private void deleteCompletedTestsFromDB(Connection connection, int testId) throws SQLException{
        String sql = "select * from completed_tests s where s.test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    deleteUserAnswersFromDB(connection, resultSet.getInt("ID"));
                }
            }
        }
        sql = "delete from completed_tests s where s.test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testId);
            statement.executeUpdate();
        }
    }

    private void deleteUserAnswersFromDB(Connection connection, int containerId) throws SQLException{
        String sql = "delete from users_answers s where s.completed_test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, containerId);
            statement.executeUpdate();
        }
    }

    private int insertCompletedTestToDB(Connection connection, User user, CompletedTestContainer testContainer) throws SQLException {
        int id;
        String sql = "insert into completed_tests (test_id, user_id, true_answers_count) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testContainer.getTest().getId());
            statement.setInt(2, user.getId());
            statement.setInt(3, testContainer.getTrueAnswersCount());
            statement.executeUpdate();
        }

        sql = "select * from completed_tests s where s.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                id = resultSet.getInt("ID");
            }
        }
        return id;
    }

    private void insertUserAnswerToDB(Connection connection, int userAnswer, int testContainerId) throws SQLException {
        String sql = "insert into users_answers (completed_test_id, answer) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, testContainerId);
            statement.setInt(2, userAnswer);
            statement.executeUpdate();
        }
    }

    private int insertTestToDB(Connection connection, Test test) throws SQLException {
        int id;
        String sql = "insert into tests (name, exercise_count) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, test.getName());
            statement.setInt(2, test.getExercises().length);
            statement.executeUpdate();
        }
        sql = "select * from tests s where s.name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, test.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                id = resultSet.getInt("ID");
            }
        }
        return id;
    }

    private int insertExerciseToDB(Connection connection, Exercise exercise, int testId) throws SQLException {
        int id;
        String sql = "insert into exercises (task, answer, answer_options_count, test_id) values (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, exercise.getTask());
            statement.setString(2, exercise.getAnswerOptions()[exercise.trueAnswerId]);
            statement.setInt(3, exercise.getAnswerOptions().length);
            statement.setInt(4, testId);
            statement.executeUpdate();
        }
        sql = "select * from exercises s where s.task = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, exercise.getTask());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                id = resultSet.getInt("ID");
            }
        }
        return id;
    }

    private void insertAnswerOptionToDB(Connection connection, String answerOption, int exerciseId) throws SQLException {
        String sql = "insert into answer_options (text, exercise_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, answerOption);
            statement.setInt(2, exerciseId);
            statement.executeUpdate();
        }
    }


    private void initTests() {
        try (Connection connection = DriverManager.getConnection(url, login, pass)) {
            String sql = "select * from tests";
            try (Statement statement = connection.createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            int testId = resultSet.getInt("ID");
                            int exerciseCount = resultSet.getInt("EXERCISE_COUNT");
                            String name = resultSet.getString("NAME");
                            Test test = new Test();
                            test.setId(testId);
                            test.setName(name);
                            test.setExercises(new Exercise[exerciseCount]);
                            initExercises(connection, test);
                            tests.add(test);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }

    private void initExercises(Connection connection, Test test) throws SQLException {
        String sql = "select * from exercises s where s.test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, test.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                int i = 0;
                while (resultSet.next()) {
                    int exerciseId = resultSet.getInt("ID");
                    String task = resultSet.getString("TASK");
                    String answer = resultSet.getString("ANSWER");
                    int answerOptionsCount = resultSet.getInt("ANSWER_OPTIONS_COUNT");
                    Exercise exercise = new Exercise();
                    exercise.setId(exerciseId);
                    exercise.setTask(task);
                    exercise.setAnswerOptions(new String[answerOptionsCount]);
                    initAnswerOptions(connection, exercise);
                    exercise.setTrueAnswerId(Arrays.asList(exercise.getAnswerOptions()).indexOf(answer));
                    test.getExercises()[i] = exercise;
                    i++;
                }
            }
        }
    }

    private void initAnswerOptions(Connection connection, Exercise exercise) throws SQLException {
        String sql = "select * from answer_options s where s.exercise_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, exercise.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                int i = 0;
                while (resultSet.next()) {
                    exercise.getAnswerOptions()[i] = resultSet.getString("TEXT");
                    i++;
                }
            }
        }
    }

    private void initUsers() {
        try (Connection conn = DriverManager.getConnection(url, login, pass)) {
            String sql = "select * from users";
            try (Statement statement = conn.createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String name = resultSet.getString("NAME");
                            String status = resultSet.getString("STATUS");
                            users.add(new User(name, id, status.equals("admin"), getUserCompletedTests(conn, id)));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }

    private List<CompletedTestContainer> getUserCompletedTests(Connection connection, int userId) throws SQLException {
        List<CompletedTestContainer> completedTests = new ArrayList<>();
        String sql = "select * from completed_tests s where s.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int containerId = resultSet.getInt("ID");
                    int testId = resultSet.getInt("TEST_ID");
                    Test test = findTestById(testId);
                    int[] userAnswers = getUserAnswers(connection, containerId, test.getExercises().length);
                    completedTests.add(new CompletedTestContainer(test, userAnswers));
                }
            }
        }
        return completedTests;
    }

    private int[] getUserAnswers(Connection connection, int containerId, int exerciseCount) throws SQLException {
        int[] userAnswers = new int[exerciseCount];
        String sql = "select * from users_answers s where s.completed_test_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, containerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                int i = 0;
                while (resultSet.next()) {
                    userAnswers[i] = resultSet.getInt("ANSWER");
                    i++;
                }
            }
        }
        return userAnswers;
    }

    private Test findTestById(int id) {
        return tests.stream()
                .filter(test -> test.getId() == id)
                .findAny()
                .orElse(null);
    }
}
